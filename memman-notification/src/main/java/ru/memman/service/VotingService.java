package ru.memman.service;

import ru.memman.dtos.AloneNewDto;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.RecipientDto;
import ru.memman.entity.AloneNew;
import ru.memman.entity.Recipient;
import ru.memman.entity.Voting;
import ru.memman.enums.Attention;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.NewsCategory;
import ru.memman.holders.StandardInfoHolder;
import ru.memman.holders.TranscriptHolder;
import ru.memman.mappers.AloneNewMapper;
import ru.memman.repository.AloneNewRepo;
import ru.memman.repository.NotificationRepo;
import ru.memman.repository.RecipientRepo;
import ru.memman.repository.VotingRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.transcriters.AbstractTranscripter;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VotingService implements SimpleStringService {
    private final NotificationRepo notificationRepo;
    private final MessageService messageService;
    private final StandardInfoHolder standardInfoHolder;
    private final AloneNewMapper aloneNewMapper;
    private final AloneNewRepo aloneNewRepo;
    private final RecipientRepo recipientRepo;
    private final VotingRepo votingRepo;
    private TranscriptHolder transcriptHolder;
    private final LinkedList<DirectiveGuards> votingDirective;

    public VotingService(NotificationRepo notificationRepo,
                         MessageService messageService,
                         StandardInfoHolder standardInfoHolder,
                         AloneNewMapper aloneNewMapper,
                         AloneNewRepo aloneNewRepo,
                         RecipientRepo recipientRepo,
                         VotingRepo votingRepo, @Qualifier("directiveVoting") LinkedList<DirectiveGuards> votingDirective) {
        this.notificationRepo = notificationRepo;
        this.messageService = messageService;
        this.standardInfoHolder = standardInfoHolder;
        this.aloneNewMapper = aloneNewMapper;
        this.aloneNewRepo = aloneNewRepo;
        this.recipientRepo = recipientRepo;
        this.votingRepo = votingRepo;
        this.votingDirective = votingDirective;
    }

    @Transactional
    public String getVoting(String user, String externId, boolean value) {
        Optional<Voting> voting = notificationRepo.findVotingWithLetter(externId);
        if (voting.isEmpty()) return "Voting is ending";

        if (value) voting.get().setAccepts(voting.get().getAccepts() + 1);
        else voting.get().setRejects(voting.get().getRejects() + 1);
        if (!ruleEndOfVoting(voting.get())) {
            String reading = voting.get().getLetter().getNotReadingString();
            removeUuidFromInfo(reading, user);
            if (reading.isBlank()) endOfVoting(voting.get());
            return "You voice is counted";
        } else endOfVoting(voting.get());
        return "Voting is done";
    }

    private boolean ruleEndOfVoting(Voting voting) {
        return voting.getGuardSize() == (voting.getAccepts() + voting.getRejects()) || voting.getGuardSize() <= 2 * Math.abs(voting.getAccepts() - voting.getRejects());
    }

    private void endContactVoting(Voting voting, boolean value) {
        List<AloneNew> lettersOfVoting = new ArrayList<>();
//                aloneNewRepo.findAllByExternId(voting.getLetter());
        if (lettersOfVoting == null || lettersOfVoting.isEmpty())
            throw new RuntimeException("linked letter with voting is absent");
        for (AloneNew alone :
                lettersOfVoting) {
            AloneNewDto letter = aloneNewMapper.entityToDto(alone);
            for (Recipient recipient :
                    alone.getSendTo()) {
                standardInfoHolder.removeMessageFromPerson(recipient.getExternUuid(), letter);
            }
        }
        if (value) {
            List<Recipient> contactPerson = lettersOfVoting.stream().flatMap(x -> x.getSendTo().stream()).toList();
            RecipientDto recipientDto = new RecipientDto();
//            contactService.addContact(contactPerson.get(0), recipientDto, contactPerson.get(1));
//            contactService.addContact(contactPerson.get(1), recipientDto, contactPerson.get(0));
            for (int i = 0; i < 2; i++) {
                AloneNew aloneNew = new AloneNew();
//                aloneNew.setAlreadyRead(false);
                aloneNew.setExternId(lettersOfVoting.get(i).getExternId().concat("i"));
                aloneNew.setCategory(NewsCategory.PRIVATE);
                aloneNew.setCreationDate(new Timestamp(System.currentTimeMillis()));
                aloneNew.setTextInfo("New contact is added. This letter is send automatic");
                aloneNew.setSubject("Contact added");
                aloneNew.setSendFrom(contactPerson.get(i));
                aloneNew.setImageUrl(contactPerson.get(i).isExistPrimePhoto() ? contactPerson.get(i).getLinkExternId() : "");
                aloneNew.setSendTo(Set.of((i == 0) ? contactPerson.get(1) : contactPerson.get(0)));
                aloneNewRepo.save(aloneNew);
                AloneNewDto letter = aloneNewMapper.entityToDto(aloneNew);
                letter.setSendingTo((i == 0) ? contactPerson.get(1).getExternUuid() : contactPerson.get(0).getExternUuid());
                letter.setSendingFromAlt(contactPerson.get(i).getNickName());
                standardInfoHolder.addNewMessageToPerson(letter, letter.getSendingTo());

//                standardInfoHolder.removeMessageFromPerson(lettersOfVoting.get(i).getSendFrom().getExternId(), aloneNewMapper.entityToDto(lettersOfVoting.get(i)));
            }
            aloneNewRepo.deleteAll(lettersOfVoting);
        } else {
            int i = (lettersOfVoting.get(0).getSubject().equals("Contact request")) ? 1 : 0;
            String nickName = (i == 0) ? lettersOfVoting.get(1).getSendTo().stream().findFirst().orElseThrow().getNickName() : lettersOfVoting.get(0).getSendTo().stream().findFirst().orElseThrow().getNickName();
            AloneNew aloneNew = lettersOfVoting.get(i);
//            aloneNew.setAlreadyRead(false);
            aloneNew.setCreationDate(new Timestamp(System.currentTimeMillis()));
            aloneNew.setTextInfo(StringUtils.join("Request for contact with ", nickName, " is rejected", ' '));
            aloneNew.setSubject("Contact reject");
            aloneNew.setExternId(lettersOfVoting.get(i).getExternId().concat("i"));
            aloneNewRepo.save(aloneNew);
            AloneNewDto letter = aloneNewMapper.entityToDto(aloneNew);
            letter.setSendingTo(aloneNew.getSendTo().stream().findFirst().orElseThrow().getExternUuid());
            letter.setSendingFromAlt("System");
            standardInfoHolder.addNewMessageToPerson(letter, letter.getSendingTo());
            if (i == 0) aloneNewRepo.delete(lettersOfVoting.get(1));
            else aloneNewRepo.delete(lettersOfVoting.get(0));
        }
        votingRepo.delete(voting);
    }


    @Transactional
    public void endOfVoting(Voting voting) {
        List<AloneNew> lettersOfVoting = notificationRepo.getAllAloneNewWithSendTo(voting.getLetterId());
        if (lettersOfVoting == null || lettersOfVoting.isEmpty())
            throw new RuntimeException("linked letters with voting is absent");
        Recipient requester = null;
        AloneNew requestLetter = null;
        AloneNew votingLetter = null;
        List<Recipient> voted = new ArrayList<>();
        for (AloneNew forLinking :
                lettersOfVoting) {
            if (forLinking.getAttention() == Attention.VOTING_REQUESTER) {
                requester = forLinking.getSendTo().stream().findFirst().orElseThrow();
                requestLetter = forLinking;
//                requester.setLinkExternId("tyt externID");
            } else {
                voted.addAll(forLinking.getSendTo());
                votingLetter = forLinking;
            }
        }
        if (requestLetter == null || votingLetter==null||requestLetter.getTextInfo() == null) throw new RuntimeException("letter(s) of voting not found");
        if (standardInfoHolder.getOnlineInfo().get(requester.getExternUuid()) != null)
            standardInfoHolder.getOnlineInfo().get(requester.getExternUuid()).removeNewMessage(aloneNewMapper.entityToDto(requestLetter));
        for (Recipient guard :
                voted) {
            if (standardInfoHolder.getOnlineInfo().get(guard.getExternUuid()) != null)
                standardInfoHolder.getOnlineInfo().get(guard.getExternUuid()).removeNewMessage(aloneNewMapper.entityToDto(votingLetter));
        }
        String[] parseText = requestLetter.getTextInfo().split("<br>");
        requestLetter.setNotReadingString(requestLetter.getSendTo().stream().map(Recipient::getExternUuid).collect(Collectors.joining(" ")));
        votingLetter.setNotReadingString(votingLetter.getSendTo().stream().map(Recipient::getExternUuid).collect(Collectors.joining(" ")));
        requestLetter.setCreationDate(new Timestamp(System.currentTimeMillis()));
        votingLetter.setCreationDate(new Timestamp(System.currentTimeMillis()));
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(requester.getLocalisation());
        String subject=transcripter.getMatrixOfTextGeneration().get("end_voting");
        requestLetter.setSubject(subject);
        votingLetter.setSubject(subject);

        DirectiveGuards directive = DirectiveGuards.builder()
                .person(lettersOfVoting.get(0).getExternId())
                .build();
        for (AloneNew alone :
                lettersOfVoting) {
            AloneNewDto letter = aloneNewMapper.entityToDto(alone);
            for (Recipient recipient :
                    alone.getSendTo()) {
                standardInfoHolder.removeMessageFromPerson(recipient.getExternUuid(), letter);
            }
        }
//        if (voting.getAccepts() >= voting.getRejects()) {
//            if (requestLetter.getAttention() != Attention.LINK) {
//                aloneNew.setTextInfo(StringUtils.join("Подтверждена "
//                        , parseText[2]
//                        , " между"
//                        , "<br>"
//                        , parseText[1]
//                        , "<br>", "и", "<br>"
//                        , parseText[3], " "));
//                directive.setOperation(KafkaOperation.ADD);
//            } else {
//                if (requester != null) {
//                    aloneNew.setTextInfo(StringUtils.join("User ", parseText[1], "теперь связан с человеком: ", parseText[3], " "));
//                    recipientRepo.save(requester);
//                    directive.setOperation(KafkaOperation.EDIT);
//                }
//            }
//            requestLetter.setAttention(Attention.VOTING_POSITIVE);
//            votingLetter.setAttention(Attention.VOTING_POSITIVE);
//        } else {
//            if (requestLetter.getAttention() != Attention.LINK) {
//                aloneNew.setTextInfo(StringUtils.join("Отклонена "
//                        , parseText[2]
//                        , " между"
//                        , "<br>"
//                        , parseText[1]
//                        , "<br>", "и", "<br>"
//                        , parseText[3], " "));
//                directive.setOperation(KafkaOperation.REMOVE);
//            } else {
//                aloneNew.setTextInfo(StringUtils.join("Связь ", parseText[1], " с человеком: ", parseText[3], " отклонена.", " "));
//                directive.setOperation(KafkaOperation.RENAME);
//            }
//            requestLetter.setAttention(Attention.VOTING_NEGATIVE);
//            votingLetter.setAttention(Attention.VOTING_NEGATIVE);
//        }
//        aloneNewRepo.deleteAll(lettersOfVoting);
//        aloneNewRepo.save(aloneNew);
//        AloneNewDto letterForGuard = aloneNewMapper.entityToDto(aloneNew);
//        for (Recipient guard :
//                aloneNew.getSendTo()) {
//            letterForGuard.setSendingTo(guard.getExternUuid());
//            letterForGuard.setSendingFromAlt("Informer");
//            standardInfoHolder.addNewMessageToPerson(letterForGuard, guard.getExternUuid());
//        }
//        votingRepo.delete(voting);
//        votingDirective.add(directive);
    }
}
