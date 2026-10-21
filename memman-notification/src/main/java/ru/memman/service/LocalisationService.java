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
import ru.memman.enums.Localisation;
import ru.memman.holders.TranscriptHolder;
import ru.memman.mappers.AloneNewMapper;
import ru.memman.mappers.LetterLocalMapper;
import ru.memman.repository.AloneNewRepository;
import ru.memman.repository.RecipientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class LocalisationService implements SimpleStringService {
    private final AloneNewMapper aloneNewMapper;
    private final LetterLocalMapper letterLocalMapper;
    private final AloneNewRepository aloneNewRepository;
    private final RecipientRepository recipientRepository;
    private final TranscriptHolder transcriptHolder;

    @Transactional(readOnly = true)
    public Optional<AloneNewDto> getLetterDtoTo(AloneNew aloneNew, Recipient recipient, Recipient from, boolean toHolder) {
        boolean alreadyRead = detectAlreadyRead(aloneNew, recipient.getExternUuid());
        log.info(aloneNew.getTextInfo());
        log.info(alreadyRead);
        log.info(aloneNew.getExternId());
        if (alreadyRead && toHolder) return Optional.empty();
        AloneNewDto letterToRecipient = AloneNewDto.builder()
                .alreadyRead(alreadyRead)
                .localisation(aloneNew.getLocalisation())
                .category(aloneNew.getCategory())
                .attention(aloneNew.getAttention())
                .creationDate(aloneNew.getCreationDate())
                .imageUrl(aloneNew.getImageUrl())
                .existPrimePhoto(aloneNew.isExistPrimePhoto())
                .externId(aloneNew.getExternId())
                .sendingFrom(from.getExternUuid())
                .sendingFromAlt(from.getNickName())
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
                    if (recipient.getLocalisation() == Localisation.EN) {
                        letterToRecipient.setSendingFromAlt(transcriptHolder.getTranscript(Localisation.EN).transcritWordFromLocalisation(from.getNickName()));
                    } else if (aloneNew.getLocalisation() == Localisation.EN) {
                        letterToRecipient.setSendingFromAlt(transcriptHolder.getTranscript(recipient.getLocalisation()).transcritWordToLocalisation(from.getNickName()));
                    } else
                        letterToRecipient.setSendingFromAlt(transcriptHolder.getTranscript(recipient.getLocalisation()).transcritWordToLocalisation(transcriptHolder.getTranscript(Localisation.EN).transcritWordFromLocalisation(from.getNickName())));
                    letterToRecipient.setLocalisation(letterLocal.getLocalisation());
                    letterToRecipient.setSubject(letterLocal.getSubject());
                    letterToRecipient.setTextInfo(letterLocal.getTextInfo());
                } else letterLocalDtoList.add(letterLocalMapper.entityToDto(letterLocal));
            }
        }
        if (letterToRecipient.getLocalisation()!=recipient.getLocalisation()) letterToRecipient.setLocalisation(recipient.getLocalisation());
        letterToRecipient.setAlreadyRead(alreadyRead);
        if (toHolder) letterToRecipient.setLetterLocals(letterLocalDtoList);
        return Optional.of(letterToRecipient);
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

    public AloneNewDto getLetterOnLocalisation(AloneNewDto letter, Localisation localisation) {
        Optional<LetterLocalDto> letterLocal;
        if (letter.getLocalisation() != localisation && (letter.getLetterLocals() != null && !letter.getLetterLocals().isEmpty())) {
            letterLocal = letter.getLetterLocals().stream()
                    .filter(x -> x.getLocalisation() == localisation)
                    .findFirst();
        } else letterLocal = Optional.empty();
        AloneNewDto letterToRecipient = AloneNewDto.builder()
                .alreadyRead(false)
                .category(letter.getCategory())
                .attention(letter.getAttention())
                .creationDate(letter.getCreationDate())
                .imageUrl(letter.getImageUrl())
                .existPrimePhoto(letter.isExistPrimePhoto())
                .externId(letter.getExternId())
                .sendingFrom(letter.getSendingFrom())
                .sendingFromAlt(letter.getSendingFromAlt())
                .sendingTo(letter.getSendingTo())
                .build();
        if (letterLocal.isPresent()) {
            letterToRecipient.setLocalisation(letterLocal.get().getLocalisation());
            letterToRecipient.setSubject(letterLocal.get().getSubject());
            letterToRecipient.setTextInfo(letterLocal.get().getTextInfo());
        } else {
            letterToRecipient.setLocalisation(letter.getLocalisation());
            letterToRecipient.setSubject(letter.getSubject());
            letterToRecipient.setTextInfo(letter.getTextInfo());
        }
        return letterToRecipient;

    }
}
