package com.example.service;

import com.example.dtos.AloneNewDto;
import com.example.dtos.Directive;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.entity.Voting;
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

    public VotingService(NotificationRepo notificationRepo,
                         MessageService messageService,
                         StandardInfoHolder standardInfoHolder,
                         AloneNewMapper aloneNewMapper,
                         AloneNewRepo aloneNewRepo,
                         RecipientRepo recipientRepo,
                         VotingRepo votingRepo, @Qualifier("directiveVoting") LinkedList<Directive> votingDirective) {
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
        Voting voting = notificationRepo.findVoting(externId);
        if (voting == null) return "Voting is ending";
        Set<String> guards = new HashSet<>();
        for (Recipient recipient :
                voting.getRecipients()) {
            guards.add(recipient.getExternId());
        }
        if (!guards.contains(user)) return "You are not guard";
        if (value) voting.setAccepts(voting.getAccepts() + 1);
        else voting.setRejects(voting.getRejects() + 1);
        if (!ruleEndOfVoting(voting)) {
            messageService.removeRecipientFromSendTo(user, externId);
            return "You voice is counted";
        }
        endOfVoting(voting);
        return "Voting is done";
    }

    private boolean ruleEndOfVoting(Voting voting) {
        return (voting.getGuardSize() / Math.abs(voting.getAccepts() - voting.getRejects())) <= 2
                || voting.getGuardSize() == (voting.getAccepts() + voting.getRejects());
    }

    private void endOfVoting(Voting voting) {
        List<AloneNew> lettersOfVoting = aloneNewRepo.findAllByExternId(voting.getLetter());
        if (lettersOfVoting == null || lettersOfVoting.isEmpty())
            throw new RuntimeException("linked letter with voting is absent");
        String[] parseText = lettersOfVoting.get(0).getTextInfo().split("<br>");
        voting.getRecipients().addAll(lettersOfVoting.get(0).getSendTo());
        voting.getRecipients().addAll(lettersOfVoting.get(1).getSendTo());
        AloneNew aloneNew = AloneNew.builder()
                .alreadyRead(false)
                .externId(voting.getLetter())
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
                standardInfoHolder.removeMessageFromPerson(recipient.getExternId(), letter);
            }
        }
        if (voting.getAccepts() >= voting.getRejects()) {
            aloneNew.setSubject("Positive result");
            if (lettersOfVoting.get(0).getSubject().equals("Link request") || lettersOfVoting.get(0).getSubject().equals("Request link")) {
                aloneNew.setTextInfo(StringUtils.join("Подтверждена "
                        , parseText[2]
                        , " между"
                        , "<br>"
                        , parseText[1]
                        , "<br>", "и", "<br>"
                        , parseText[3], " "));
                directive.setOperation(KafkaOperation.ADD);
            } else {
                aloneNew.setTextInfo(StringUtils.join("User ", parseText[1], "теперь связан с человеком: ", parseText[4], " "));
                directive.setOperation(KafkaOperation.EDIT);
            }
        } else {
            aloneNew.setSubject("Negative result");
            if (lettersOfVoting.get(0).getSubject().equals("Link request") || lettersOfVoting.get(0).getSubject().equals("Request link")) {
                aloneNew.setTextInfo(StringUtils.join("Отклонена "
                        , parseText[2]
                        , " между"
                        , "<br>"
                        , parseText[1]
                        , "<br>", "и", "<br>"
                        , parseText[3], " "));
                directive.setOperation(KafkaOperation.REMOVE);
            } else {
                aloneNew.setTextInfo(StringUtils.join("Связь ", parseText[1], " с человеком: ", parseText[4], " отклонена.", " "));
                directive.setOperation(KafkaOperation.RENAME);
            }
        }
        aloneNewRepo.deleteAll(lettersOfVoting);
        aloneNewRepo.save(aloneNew);
        AloneNewDto letterForGuard = aloneNewMapper.entityToDto(aloneNew);
        for (Recipient guard :
                aloneNew.getSendTo()) {
            letterForGuard.setSendingTo(guard.getExternId());
            letterForGuard.setSendingFromAlt("Informer");
            standardInfoHolder.addNewMessageToPerson(letterForGuard);
        }
        votingRepo.delete(voting);
        votingDirective.add(directive);
    }
}
