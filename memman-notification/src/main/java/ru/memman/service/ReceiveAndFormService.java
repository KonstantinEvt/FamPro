package ru.memman.service;

import ru.memman.dtos.AloneNewDto;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.entity.AloneNew;
import ru.memman.entity.LetterLocal;
import ru.memman.entity.Recipient;
import ru.memman.entity.Voting;
import ru.memman.enums.*;
import ru.memman.holders.StandardInfoHolder;
import ru.memman.holders.TranscriptHolder;
import ru.memman.repository.AloneNewRepo;
import ru.memman.repository.NotificationRepo;
import ru.memman.repository.VotingRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.transcriters.AbstractTranscripter;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Log4j2
public class ReceiveAndFormService implements SimpleStringService {
    private RecipientService recipientService;
    private AloneNewRepo aloneNewRepo;
    private NotificationRepo notificationRepo;
    private VotingRepo votingRepo;
    private StandardInfoHolder standardInfoHolder;
    private LocalisationService localisationService;
    private TranscriptHolder transcriptHolder;
    private MessageService messageService;

    public void putLetterToHolder(AloneNew letter, Recipient recipient) {
        boolean inHolder = standardInfoHolder.getOnlineInfo().containsKey(recipient.getExternUuid());
        if (inHolder) {
            Optional<AloneNewDto> letterDto = localisationService.getLetterDtoTo(letter, recipient, null, true);
            letterDto.ifPresent(x -> {standardInfoHolder.addNewMessageToPerson(x, recipient.getExternUuid());messageService.sendChangeLetterCounts(recipient.getExternUuid());});
        }
    }

    @Transactional
    public void receiveAttentionLetter(DirectiveGuards directiveGuards) {
        Recipient recipient = recipientService.findRecipient(directiveGuards.getTokenUser());
        if (recipient == null) throw new RuntimeException("recipient is absent");
        AloneNew aloneNew = formAnswerByInformer(directiveGuards, NewsCategory.PRIVATE, recipient);
        putLetterToHolder(aloneNew, recipient);
    }

    @Transactional
    public void receiveVotingLetter(DirectiveGuards directiveGuards) {
        if (directiveGuards.getTokenUser() == null || directiveGuards.getGuards() == null || directiveGuards.getGuards().isEmpty())
            throw new RuntimeException("wrong directive");
        Set<String> directiveMembers = new HashSet<>(directiveGuards.getGuards());
        directiveMembers.add(directiveGuards.getTokenUser());
        Set<Recipient> directiveRecipients = notificationRepo.findGuards(directiveMembers);
        Recipient requester = directiveRecipients.stream().filter(x -> Objects.equals(x.getExternUuid(), directiveGuards.getTokenUser())).findFirst().orElseThrow(() -> new RuntimeException("recipient not found"));
        directiveRecipients.remove(requester);
        if (directiveRecipients.isEmpty()) throw new RuntimeException("guards not found");
        putLetterToHolder(formAnswerByInformer(directiveGuards, NewsCategory.PRIVATE, requester), requester);
        AloneNew aloneNewGuards = formMessageToGuards(directiveGuards, directiveRecipients, requester);
        for (Recipient guard : directiveRecipients) {
            putLetterToHolder(aloneNewGuards, guard);
        }
    }

    @Transactional
    public AloneNew formMessageToGuards(DirectiveGuards directiveGuards, Set<Recipient> guards, Recipient requester) {
        AloneNew aloneNew = buildInformLetter(directiveGuards, guards, requester, true);
        if (directiveGuards.getSwitchPosition() == SwitchPosition.BIRTH) aloneNew.setAttention(Attention.LINK);
        else aloneNew.setAttention(Attention.VOTING);
        aloneNew.setCategory(NewsCategory.FAMILY);
        aloneNewRepo.save(aloneNew);
        votingRepo.save(Voting.builder()
                .letter(aloneNew)
                .letterId(aloneNew.getExternId())
                .guardSize(directiveGuards.getGuards().size())
                .accepts(0)
                .rejects(0)
                .build());
        return aloneNew;
    }

    private String formReserveInfo(DirectiveGuards directiveGuards, Recipient requester, Boolean forGuards) {
        String info = ((requester.getNickName()==null)?"!":requester.getNickName().concat("!")).concat(requester.getExternUuid()).concat("!");
        if (directiveGuards.getInfo1() != null && !directiveGuards.getInfo1().isBlank())
            info = info.concat(directiveGuards.getInfo1());
        info = info.concat("!");
        if (directiveGuards.getInfo2() != null && !directiveGuards.getInfo2().isBlank())
            info = info.concat(directiveGuards.getInfo2());
        info = info.concat("!");
        if (directiveGuards.getInfo3() != null && !directiveGuards.getInfo3().isBlank())
            info = info.concat(directiveGuards.getInfo3());
        info = info.concat("!");
        if (directiveGuards.getNumber1() != 0L) info = info.concat(String.valueOf(directiveGuards.getNumber1()));
        info = info.concat("!").concat((forGuards) ? "1" : "0");
        return info;
    }

    private String formInfo(AbstractTranscripter transcripter, DirectiveGuards directiveGuards, Recipient requester, Boolean forGuards) {
        String info;
        if (forGuards) {
            if (requester.getNickName() == null || requester.getNickName().isBlank())
                info = String.join("", transcripter.getUnknown(), "&nbsp;", transcripter.getUser().toLowerCase());
            else info = String.join("", transcripter.getMatrixOfTextGeneration().get("USER_NICK"),
                    requester.getNickName());
            info = String.join("", info,
                    transcripter.getMatrixOfTextGeneration().get("USER_NICK1"),
                    (directiveGuards.getSwitchPosition() == SwitchPosition.BIRTH) ? transcripter.getMatrixOfTextGeneration().get("USER_NICK2") : transcripter.getMatrixOfTextGeneration().get("USER_NICK3"),
                    directiveGuards.getInfo1());
            if (directiveGuards.getSwitchPosition() != SwitchPosition.BIRTH) {
                if (directiveGuards.getInfo3() != null && directiveGuards.getInfo2() != null)
                    info = String.join("", info,
                            transcripter.getAnd(),
                            transcripter.getAbsent(),
                            directiveGuards.getInfo2(),
                            transcripter.getAnd(),
                            directiveGuards.getInfo3());
                else if (directiveGuards.getInfo2() != null)
                    info = String.join("", transcripter.getAnd(), directiveGuards.getInfo2());
            }
            info = String.join("", info, "<br>", transcripter.getElection());
        } else {
            info = transcripter.getMatrixOfTextGeneration().get(directiveGuards.getSubject().name().concat("1"));
            if (directiveGuards.getInfo1() != null) info=String.join("", info, directiveGuards.getInfo1(),transcripter.getMatrixOfTextGeneration().get(directiveGuards.getSubject().name().concat("2")));
            if (directiveGuards.getSwitchPosition() != SwitchPosition.BIRTH) {
                if (directiveGuards.getInfo3() != null && directiveGuards.getInfo2() != null)
                    info = String.join("", info,
                            transcripter.getAnd(),
                            transcripter.getAbsent(),
                            directiveGuards.getInfo2(),
                            transcripter.getAnd(),
                            directiveGuards.getInfo3());
                else if (directiveGuards.getInfo2() != null)
                    info = String.join("", transcripter.getAnd(), directiveGuards.getInfo2());
            }
            if (directiveGuards.getNumber1()!=0L) info=info.concat(String.valueOf(directiveGuards.getNumber1()));
        }
        return info;
    }
    private AloneNew buildInformLetter(DirectiveGuards directiveGuards, Set<Recipient> recipientSet, Recipient requester, Boolean forGuards) {
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(requester.getLocalisation());
        AloneNew aloneNew = AloneNew.builder()
                .externId(directiveGuards.getId())
                .creationDate(directiveGuards.getCreated())
                .existLocalisations(true)
                .existPrimePhoto(false)
                .sendTo(recipientSet)
                .notReadingString((forGuards)?getInfoStringFromStringUuids(recipientSet.stream().map(Recipient::getExternUuid).collect(Collectors.toSet())):requester.getExternUuid())
                .sendFrom(null)
                .subject(transcripter.getTextSubject(directiveGuards.getSubject()))
                .textInfo(formInfo(transcripter, directiveGuards, requester, forGuards))
                .letterLocalisations(new ArrayList<>())
                .localisation(directiveGuards.getLocalisation())
                .build();
        LetterLocal letterLocal = LetterLocal.builder()
                .localisation(Localisation.OTHER)
                .subject(directiveGuards.getSubject().name())
                .textInfo(formReserveInfo(directiveGuards, requester, forGuards))
                .mainLetter(aloneNew)
                .build();
        aloneNew.getLetterLocalisations().add(letterLocal);
        return aloneNew;
    }

    @Transactional
    public AloneNew formAnswerByInformer(DirectiveGuards directiveGuards, NewsCategory category, Recipient recipient) {
        AloneNew aloneNew = buildInformLetter(directiveGuards, Set.of(recipient), recipient, false);
        switch (directiveGuards.getSwitchPosition()) {
            case MAIN -> {
                aloneNew.setAttention(Attention.MODERATE);
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
            case FATHER -> {
                aloneNew.setAttention(Attention.RIGHTS);
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
            case MOTHER -> {
                aloneNew.setAttention(Attention.NEGATIVE);
                aloneNew.setCategory(Objects.requireNonNullElse(category, NewsCategory.PRIVATE));
            }
            case PRIME -> {
                aloneNew.setCategory(NewsCategory.PRIVATE);
                if (directiveGuards.getPerson() != null) {
                    aloneNew.setAttention(Attention.POSITIVE);
                } else {
                    aloneNew.setAttention(Attention.NEGATIVE);
                }
            }
            case BIRTH -> {
                if (directiveGuards.getOperation()==KafkaOperation.EDIT) aloneNew.setAttention(Attention.LINK);
                else aloneNew.setAttention(Attention.POSITIVE);
                aloneNew.setCategory(Objects.requireNonNullElse(category, NewsCategory.PRIVATE));
            }
            case ADDRESS -> {
                aloneNew.setAttention(Attention.DATE);
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
            case BURIAL -> {
                aloneNew.setAttention(Attention.VOTING_REQUESTER);
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
            default -> {
                aloneNew.setAttention(Attention.UNKNOWN);
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
        }
        return aloneNewRepo.save(aloneNew);
    }
}
