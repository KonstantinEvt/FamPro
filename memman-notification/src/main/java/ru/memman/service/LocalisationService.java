package ru.memman.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.dtos.AloneNewDto;
import ru.memman.dtos.LetterLocalDto;
import ru.memman.entity.AloneNew;
import ru.memman.entity.LetterLocal;
import ru.memman.entity.Recipient;
import ru.memman.enums.Attention;
import ru.memman.enums.Localisation;
import ru.memman.enums.NewsCategory;
import ru.memman.enums.Subject;
import ru.memman.holders.TranscriptHolder;
import ru.memman.mappers.LetterLocalMapper;
import ru.memman.transcriters.AbstractTranscripter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class LocalisationService implements SimpleStringService {
    private final LetterLocalMapper letterLocalMapper;
    private final TranscriptHolder transcriptHolder;
    private final Recipient informerRecipientHard;

    @Transactional(readOnly = true)
    public Optional<AloneNewDto> getLetterDtoTo(AloneNew aloneNew, Recipient recipient, Recipient from, boolean toHolder) {
        boolean alreadyRead = detectAlreadyRead(aloneNew, recipient.getExternUuid());
        log.info(aloneNew.getTextInfo());
        log.info(alreadyRead);
        log.info(aloneNew.getExternId());
        if (alreadyRead && toHolder) return Optional.empty();
        AloneNewDto letterToRecipient = AloneNewDto.builder()
                .id(aloneNew.getId())
                .alreadyRead(alreadyRead)
                .localisation(aloneNew.getLocalisation())
                .category(aloneNew.getCategory())
                .attention(aloneNew.getAttention())
                .creationDate(aloneNew.getCreationDate())
                .imageUrl(aloneNew.getImageUrl())
                .existPrimePhoto(aloneNew.isExistPrimePhoto())
                .postProcess(aloneNew.isPostProcess())
                .externId(aloneNew.getExternId())
                .sendingFrom((from == null) ? informerRecipientHard.getExternUuid() : from.getExternUuid())
                .sendingFromAlt((from == null) ? informerRecipientHard.getNickName() : from.getNickName())
                .existLocalisations(aloneNew.isExistLocalisations())
                .subject(aloneNew.getSubject())
                .textInfo(aloneNew.getTextInfo())
                .build();
        List<LetterLocalDto> letterLocalDtoList = new ArrayList<>();
        if (aloneNew.isExistLocalisations() && aloneNew.getLetterLocalisations() != null && !aloneNew.getLetterLocalisations().isEmpty()) {
            for (LetterLocal letterLocal : aloneNew.getLetterLocalisations()) {
                if (letterLocal.getLocalisation() == recipient.getLocalisation()) {
                    letterLocalDtoList.add(LetterLocalDto.builder()
                            .localisation(letterToRecipient.getLocalisation())
                            .subject(letterToRecipient.getSubject())
                            .textInfo(letterToRecipient.getTextInfo())
                            .build());
                    letterToRecipient.setLocalisation(letterLocal.getLocalisation());
                    letterToRecipient.setSubject(letterLocal.getSubject());
                    letterToRecipient.setTextInfo(letterLocal.getTextInfo());
                } else letterLocalDtoList.add(letterLocalMapper.entityToDto(letterLocal));
            }
        }
        if (toHolder || from == null) letterToRecipient.setLetterLocals(letterLocalDtoList);
        if (from != null && letterToRecipient.getCategory() != NewsCategory.COMMON && letterToRecipient.getCategory() != NewsCategory.SYSTEM)
            transcriptNickForLetter(recipient.getLocalisation(), from.getLocalisation(), letterToRecipient.getSendingFromAlt(), letterToRecipient);
        else if (from == null && letterToRecipient.getLocalisation() != recipient.getLocalisation()) {
            getLetterDtoByInformer(letterToRecipient, recipient.getLocalisation());
            letterToRecipient.setLocalisation(recipient.getLocalisation());
        }
        if (letterToRecipient.getLocalisation() != recipient.getLocalisation()) {
            if (letterToRecipient.getTextInfo() == null) letterToRecipient.setTextInfo("");
            letterToRecipient.setTextInfo(transcriptHolder.getTranscript(recipient.getLocalisation()).getAbsentLocalisation().concat(letterToRecipient.getTextInfo()));
            letterToRecipient.setLocalisation(recipient.getLocalisation());
        }
        letterToRecipient.setAlreadyRead(alreadyRead);
        return Optional.of(letterToRecipient);
    }

    public void transcriptNickForLetter(Localisation to, Localisation from, String fromNick, AloneNewDto aloneNewDto) {
//        if (!Objects.equals(aloneNewDto.getSendingFrom(), systemRecipientHard.getExternUuid()) || !Objects.equals(aloneNewDto.getSendingFrom(), informerRecipientHard.getExternUuid())) {
        if (to == Localisation.EN) {
            aloneNewDto.setSendingFromAlt(transcriptHolder.getTranscript(Localisation.EN).transcritWordFromLocalisation(fromNick));
        } else if (from == Localisation.EN) {
            aloneNewDto.setSendingFromAlt(transcriptHolder.getTranscript(to).transcritWordToLocalisation(fromNick));
        } else
            aloneNewDto.setSendingFromAlt(transcriptHolder.getTranscript(to).transcritWordToLocalisation(transcriptHolder.getTranscript(Localisation.EN).transcritWordFromLocalisation(fromNick)));
//        }
    }

    public boolean detectAlreadyRead(AloneNew aloneNew, String externUuid) {
        if (!aloneNew.isAlreadyRead()) {
            Optional<String> reading = getAllStringUuidFromInfo(aloneNew.getNotReadingString())
                    .stream()
                    .filter(x -> Objects.equals(externUuid, x))
                    .findFirst();
            return (reading.isEmpty());
        }
        return true;
    }

    public Subject findSubjectByName(String name) {
        Subject findSub = Subject.UNKNOWN;
        for (Subject sub :
                Subject.values()) {
            if (Objects.equals(name, sub.name())) findSub = sub;
        }
        return findSub;
    }

    public void getLetterDtoByInformer(AloneNewDto letterToRecipient, Localisation localisation) {
        if (letterToRecipient.getLetterLocals() == null) {
            log.warn("битое inform message. Нет reserve localisation ");
            return;
        }
        LetterLocalDto reserveLetter = letterToRecipient.getLetterLocals().stream().findFirst().orElseThrow(() -> new RuntimeException("corrupt inform message"));
        if (reserveLetter.getLocalisation() == Localisation.OTHER)
            reserveLetter.setLocalisation(letterToRecipient.getLocalisation());
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(reserveLetter.getLocalisation());
        Subject findSub = findSubjectByName(reserveLetter.getSubject());
        String reserveInfoString;
        if (reserveLetter.getLocalisation() != Localisation.EN)
            reserveInfoString = transcripter.transcritWordFromLocalisation(reserveLetter.getTextInfo());
        else reserveInfoString = reserveLetter.getTextInfo();

        transcripter = transcriptHolder.getTranscript(localisation);
        letterToRecipient.setSubject(transcripter.getTextSubject(findSub));
        if (localisation != Localisation.EN)
            reserveInfoString = transcripter.transcritWordToLocalisation(reserveInfoString);
        letterToRecipient.setTextInfo(generateTextOfLetter(reserveInfoString, transcripter, findSub, letterToRecipient.getAttention()));
    }

    public String generateTextOfLetter(String reserveInfoString, AbstractTranscripter transcripter, Subject subject, Attention attention) {
        String formOfLetter;
        if (subject == Subject.END_VOTING) formOfLetter = subject.name();
        else formOfLetter = "USER_NICK";
        String[] strings = reserveInfoString.split("!");
        String info;
        if (Objects.equals(strings[6], "1")) {
            if (strings[0] == null || strings[0].isBlank())
                info = String.join("", transcripter.getUnknown(), "&nbsp;", transcripter.getUser().toLowerCase());
            else info = String.join("", transcripter.getMatrixOfTextGeneration().get(formOfLetter),
                    strings[0]);
            info = String.join("", info,
                    transcripter.getMatrixOfTextGeneration().get(formOfLetter.concat("1")),
                    (attention == Attention.LINK) ? transcripter.getMatrixOfTextGeneration().get(formOfLetter.concat("2")) : transcripter.getMatrixOfTextGeneration().get(formOfLetter.concat("3")),
                    "<em>", strings[2], "</em>");
            if (attention != Attention.LINK) {
                if (strings[4] != null && !strings[4].isBlank()
                        && strings[3] != null && !strings[3].isBlank())
                    info = formAddDoubleTextName(info, strings[3], strings[4], transcripter);
                else if (strings[3] != null && !strings[3].isBlank())
                    info = formAddSingleTextName(info, strings[3], transcripter);
            }
            if (subject != Subject.END_VOTING) info = String.join("", info, "<br>", transcripter.getElection());
        } else {
            if (subject == Subject.END_VOTING
                    && (attention == Attention.VOTING_REQUESTER || attention == Attention.CONTACT_REQUESTER || attention == Attention.LINK_REQUESTER)) {
                info = transcripter.getMatrixOfTextGeneration().get(attention.name().concat("1"));
                if (strings[2] != null && !strings[2].isBlank())
                    info = String.join("", info,
                            "<em>", strings[2], "</em>",
                            transcripter.getMatrixOfTextGeneration().get(attention.name().concat("2")));
            } else {
                info = transcripter.getMatrixOfTextGeneration().get(subject.name().concat("1"));
                if (strings[2] != null && !strings[2].isBlank())
                    info = String.join("", info,
                            "<em>", strings[2], "</em>",
                            transcripter.getMatrixOfTextGeneration().get(subject.name().concat("2")));
            }
            if (attention == Attention.VOTING_REQUESTER) {
                if (strings[4] != null && !strings[4].isBlank()
                        && strings[3] != null && !strings[3].isBlank())
                    info = formAddDoubleTextName(info, strings[3], strings[4], transcripter);
                else if (strings[3] != null && !strings[3].isBlank())
                    info = formAddSingleTextName(info, strings[3], transcripter);
            }
            if (!Objects.equals(strings[5], "0")) info = info.concat(String.valueOf(strings[5]));
        }
        return info;
    }

    public String formAddSingleTextName(String info, String name, AbstractTranscripter transcripter) {
        return String.join("", info,
                transcripter.getAnd(),
                "<em>", name, "</em>");
    }

    public String formAddDoubleTextName(String info, String name1, String name2, AbstractTranscripter transcripter) {
        return String.join("", info,
                transcripter.getAnd(),
                transcripter.getAbsent(),
                "<em>", name1, "</em>",
                transcripter.getAnd(),
                "<em>", name2, "</em>");
    }

    public AloneNewDto getLetterOnLocalisation(AloneNewDto letter, Localisation localisation) {
        AloneNewDto letterToRecipient = AloneNewDto.builder()
                .id(letter.getId())
                .alreadyRead(false)
                .category(letter.getCategory())
                .attention(letter.getAttention())
                .creationDate(letter.getCreationDate())
                .imageUrl(letter.getImageUrl())
                .existPrimePhoto(letter.isExistPrimePhoto())
                .postProcess(letter.isPostProcess())
                .externId(letter.getExternId())
                .sendingFrom(letter.getSendingFrom())
                .sendingFromAlt(letter.getSendingFromAlt())
                .build();
        Optional<LetterLocalDto> letterLocal;
        if (letter.getLocalisation() != localisation
                && (letter.getLetterLocals() != null
                && !letter.getLetterLocals().isEmpty()
                && !Objects.equals(letter.getSendingFrom(), informerRecipientHard.getExternUuid()))) {
            letterLocal = letter.getLetterLocals().stream()
                    .filter(x -> x.getLocalisation() == localisation)
                    .findFirst();
        } else letterLocal = Optional.empty();
        if (letter.getLocalisation() != localisation
                && letter.getCategory() != NewsCategory.SYSTEM && letter.getCategory() != NewsCategory.COMMON
                && !Objects.equals(letter.getSendingFrom(), informerRecipientHard.getExternUuid()))
            transcriptNickForLetter(localisation, letter.getLocalisation(), letter.getSendingFromAlt(), letter);
        if (letterLocal.isPresent()) {
            letterToRecipient.setLocalisation(letterLocal.get().getLocalisation());
            letterToRecipient.setSubject(letterLocal.get().getSubject());
            letterToRecipient.setTextInfo(letterLocal.get().getTextInfo());
        } else {
            letterToRecipient.setSubject(letter.getSubject());
            if (letter.getLocalisation() != localisation) {
                if (Objects.equals(letterToRecipient.getSendingFrom(), informerRecipientHard.getExternUuid())) {
                    letterToRecipient.setLocalisation(letter.getLocalisation());
                    letterToRecipient.setLetterLocals(letter.getLetterLocals());
                    getLetterDtoByInformer(letterToRecipient, localisation);
                    log.info("letter from info will be translate");
                } else {
                    if (letterToRecipient.getTextInfo() == null) letterToRecipient.setTextInfo("");
                    letterToRecipient.setTextInfo(transcriptHolder.getTranscript(localisation).getAbsentLocalisation().concat(letter.getTextInfo()));
                    log.info("random letter translate");
                }
                letterToRecipient.setLocalisation(localisation);
            } else {
                letterToRecipient.setTextInfo(letter.getTextInfo());
                log.warn("fast change fatal localisation= {}", localisation);
            }
        }
//        letterToRecipient.setSendingFrom(informerRecipientHard.getExternUuid());
        return letterToRecipient;
    }
}
