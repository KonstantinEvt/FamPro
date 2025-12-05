package com.example.service;

import com.example.dtos.AloneNewDto;
import com.example.dtos.Directive;
import com.example.dtos.RecipientDto;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.entity.Voting;
import com.example.enums.Attention;
import com.example.enums.KafkaOperation;
import com.example.enums.NewsCategory;
import com.example.holders.StandardInfoHolder;
import com.example.mappers.AloneNewMapper;
import com.example.repository.AloneNewRepo;
import com.example.repository.NotificationRepo;
import com.example.repository.RecipientRepo;
import com.example.repository.VotingRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class VotingService {
    private final NotificationRepo notificationRepo;
    private final MessageService messageService;
    private final StandardInfoHolder standardInfoHolder;
    private final AloneNewMapper aloneNewMapper;
    private final AloneNewRepo aloneNewRepo;
    private final RecipientRepo recipientRepo;
    private final VotingRepo votingRepo;
    private final LinkedList<Directive> votingDirective;
    private final ContactService contactService;

    public VotingService(NotificationRepo notificationRepo,
                         MessageService messageService,
                         StandardInfoHolder standardInfoHolder,
                         AloneNewMapper aloneNewMapper,
                         AloneNewRepo aloneNewRepo,
                         RecipientRepo recipientRepo,
                         VotingRepo votingRepo, @Qualifier("directiveVoting") LinkedList<Directive> votingDirective, ContactService contactService) {
        this.notificationRepo = notificationRepo;
        this.messageService = messageService;
        this.standardInfoHolder = standardInfoHolder;
        this.aloneNewMapper = aloneNewMapper;
        this.aloneNewRepo = aloneNewRepo;
        this.recipientRepo = recipientRepo;
        this.votingRepo = votingRepo;
        this.votingDirective = votingDirective;
        this.contactService = contactService;
    }

    @Transactional
    public String getVoting(String user, String externId, boolean value) {
        Voting voting = notificationRepo.findVoting(externId);
        if (voting == null) return "Voting is ending";
        if (voting.getLetter().length() == user.length()) {
            Set<String> guards = new HashSet<>();
            for (Recipient recipient :
                    voting.getRecipients()) {
                guards.add(recipient.getExternUuid());
            }
            if (!guards.contains(user)) return "You are not guard";
            if (value) voting.setAccepts(voting.getAccepts() + 1);
            else voting.setRejects(voting.getRejects() + 1);
            if (!ruleEndOfVoting(voting)) {
                messageService.removeRecipientFromSendTo(user, externId);
                return "You voice is counted";
            }
            endOfVoting(voting);
        } else endContactVoting(voting, value);
        return "Voting is done";
    }

    private void endContactVoting(Voting voting, boolean value) {
        List<AloneNew> lettersOfVoting = aloneNewRepo.findAllByExternId(voting.getLetter());
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
            contactService.addContact(contactPerson.get(0), recipientDto, contactPerson.get(1));
            contactService.addContact(contactPerson.get(1), recipientDto, contactPerson.get(0));
            for (int i = 0; i < 2; i++) {
                AloneNew aloneNew =new AloneNew();
                aloneNew.setAlreadyRead(false);
                aloneNew.setExternId(lettersOfVoting.get(i).getExternId().concat("i"));
                aloneNew.setCategory(NewsCategory.PRIVATE);
                aloneNew.setCreationDate(new Timestamp(System.currentTimeMillis()));
                aloneNew.setTextInfo("New contact is added. This letter is send automatic");
                aloneNew.setSubject("Contact added");
                aloneNew.setSendFrom(contactPerson.get(i));
                aloneNew.setImageUrl(contactPerson.get(i).isUrlPhoto() ? contactPerson.get(i).getLinkExternId() : "");
                aloneNew.setSendTo(Set.of((i == 0) ? contactPerson.get(1) : contactPerson.get(0)));
                aloneNewRepo.save(aloneNew);
                AloneNewDto letter = aloneNewMapper.entityToDto(aloneNew);
                letter.setSendingTo((i == 0) ? contactPerson.get(1).getExternUuid() : contactPerson.get(0).getExternUuid());
                letter.setSendingFromAlt(contactPerson.get(i).getNickName());
                standardInfoHolder.addNewMessageToPerson(letter);

//                standardInfoHolder.removeMessageFromPerson(lettersOfVoting.get(i).getSendFrom().getExternId(), aloneNewMapper.entityToDto(lettersOfVoting.get(i)));
            } aloneNewRepo.deleteAll(lettersOfVoting);
        } else {
            int i = (lettersOfVoting.get(0).getSubject().equals("Contact request")) ? 1 : 0;
            String nickName = (i == 0) ? lettersOfVoting.get(1).getSendTo().stream().findFirst().orElseThrow().getNickName() : lettersOfVoting.get(0).getSendTo().stream().findFirst().orElseThrow().getNickName();
            AloneNew aloneNew = lettersOfVoting.get(i);
            aloneNew.setAlreadyRead(false);
            aloneNew.setCreationDate(new Timestamp(System.currentTimeMillis()));
            aloneNew.setTextInfo(StringUtils.join("Request for contact with ", nickName, " is rejected",' '));
            aloneNew.setSubject("Contact reject");
            aloneNew.setExternId(lettersOfVoting.get(i).getExternId().concat("i"));
            aloneNewRepo.save(aloneNew);
            AloneNewDto letter = aloneNewMapper.entityToDto(aloneNew);
            letter.setSendingTo(aloneNew.getSendTo().stream().findFirst().orElseThrow().getExternUuid());
            letter.setSendingFromAlt("System");
            standardInfoHolder.addNewMessageToPerson(letter);
            if (i == 0) aloneNewRepo.delete(lettersOfVoting.get(1));
            else aloneNewRepo.delete(lettersOfVoting.get(0));
        }
        votingRepo.delete(voting);
    }

    private boolean ruleEndOfVoting(Voting voting) {
        return (voting.getGuardSize() / Math.abs(voting.getAccepts() - voting.getRejects())) <= 2
                || voting.getGuardSize() == (voting.getAccepts() + voting.getRejects());
    }

    private void endOfVoting(Voting voting) {
        List<AloneNew> lettersOfVoting = aloneNewRepo.findAllByExternId(voting.getLetter());
        if (lettersOfVoting == null || lettersOfVoting.isEmpty())
            throw new RuntimeException("linked letter with voting is absent");
        Recipient requester = null;
        String extern = null;
        for (AloneNew forLinking :
                lettersOfVoting) {
            if (forLinking.getAttention()== Attention.VOTING) {
                requester = forLinking.getSendTo().stream().findFirst().orElseThrow();
//                requester.setUrlPhoto(forLinking.getImageUrl().equals("1"));
            }
            if (forLinking.getAttention()==Attention.VOTING_REQUESTER) extern = forLinking.getImageUrl();
        }
        if (requester != null) requester.setLinkExternId(extern);
        String[] parseText = lettersOfVoting.get(0).getTextInfo().split("<br>");
        voting.getRecipients().addAll(lettersOfVoting.get(0).getSendTo());
        voting.getRecipients().addAll(lettersOfVoting.get(1).getSendTo());
        AloneNew aloneNew = AloneNew.builder()
                .alreadyRead(false)
                .externId(voting.getLetter())
                .subject("Голосование завершено")
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .category(NewsCategory.FAMILY)
                .sendFrom(recipientRepo.findById(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .sendTo(voting.getRecipients())
                .build();
        Directive directive = Directive.builder()
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
        if (voting.getAccepts() >= voting.getRejects()) {
            aloneNew.setAttention(Attention.VOTING_POSITIVE);
            if (lettersOfVoting.get(0).getAttention()!=Attention.LINK && lettersOfVoting.get(1).getAttention()!=Attention.LINK) {
                aloneNew.setTextInfo(StringUtils.join("Подтверждена "
                        , parseText[2]
                        , " между"
                        , "<br>"
                        , parseText[1]
                        , "<br>", "и", "<br>"
                        , parseText[3], " "));
                directive.setOperation(KafkaOperation.ADD);
            } else {
                if (requester != null) {
                    aloneNew.setTextInfo(StringUtils.join("User ", parseText[1], "теперь связан с человеком: ", parseText[3], " "));
                    recipientRepo.save(requester);
                    directive.setOperation(KafkaOperation.EDIT);
                }
            }
        } else {
            aloneNew.setAttention(Attention.VOTING_NEGATIVE);
            if (lettersOfVoting.get(0).getAttention()!=Attention.LINK && lettersOfVoting.get(1).getAttention()!=Attention.LINK) {
                aloneNew.setTextInfo(StringUtils.join("Отклонена "
                        , parseText[2]
                        , " между"
                        , "<br>"
                        , parseText[1]
                        , "<br>", "и", "<br>"
                        , parseText[3], " "));
                directive.setOperation(KafkaOperation.REMOVE);
            } else {
                aloneNew.setTextInfo(StringUtils.join("Связь ", parseText[1], " с человеком: ", parseText[3], " отклонена.", " "));
                directive.setOperation(KafkaOperation.RENAME);
            }
        }
        aloneNewRepo.deleteAll(lettersOfVoting);
        aloneNewRepo.save(aloneNew);
        AloneNewDto letterForGuard = aloneNewMapper.entityToDto(aloneNew);
        for (Recipient guard :
                aloneNew.getSendTo()) {
            letterForGuard.setSendingTo(guard.getExternUuid());
            letterForGuard.setSendingFromAlt("Informer");
            standardInfoHolder.addNewMessageToPerson(letterForGuard);
        }
        votingRepo.delete(voting);
        votingDirective.add(directive);
    }
}
