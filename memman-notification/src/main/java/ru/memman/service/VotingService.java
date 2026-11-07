package ru.memman.service;

import lombok.extern.log4j.Log4j2;
import ru.memman.dtos.AloneNewDto;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.RecipientDto;
import ru.memman.entity.AloneNew;
import ru.memman.entity.LetterLocal;
import ru.memman.entity.Recipient;
import ru.memman.entity.Voting;
import ru.memman.enums.Attention;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.NewsCategory;
import ru.memman.enums.Subject;
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
@Log4j2
public class VotingService implements SimpleStringService {
    private final NotificationRepo notificationRepo;
    private final LocalisationService localisationService;
    private final MessageService messageService;
    private final StandardInfoHolder standardInfoHolder;
    private final AloneNewMapper aloneNewMapper;
    private final AloneNewRepo aloneNewRepo;
    private final RecipientRepo recipientRepo;
    private final VotingRepo votingRepo;
    private final TranscriptHolder transcriptHolder;
    private final LinkedList<DirectiveGuards> votingDirective;

    public VotingService(NotificationRepo notificationRepo,
                         LocalisationService localisationService, MessageService messageService,
                         StandardInfoHolder standardInfoHolder,
                         AloneNewMapper aloneNewMapper,
                         AloneNewRepo aloneNewRepo,
                         RecipientRepo recipientRepo,
                         VotingRepo votingRepo,
                         TranscriptHolder transcriptHolder,
                         @Qualifier("directiveVoting") LinkedList<DirectiveGuards> votingDirective) {
        this.notificationRepo = notificationRepo;
        this.localisationService = localisationService;
        this.messageService = messageService;
        this.standardInfoHolder = standardInfoHolder;
        this.aloneNewMapper = aloneNewMapper;
        this.aloneNewRepo = aloneNewRepo;
        this.recipientRepo = recipientRepo;
        this.votingRepo = votingRepo;
        this.transcriptHolder = transcriptHolder;
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

     @Transactional
    public void endOfVoting(Voting voting) {
        List<AloneNew> lettersOfVoting = notificationRepo.getAllAloneNewWithSendTo(voting.getLetterExternId());
        if (lettersOfVoting == null || lettersOfVoting.isEmpty())
            throw new RuntimeException("linked letters with voting is absent");

        AloneNew requestLetter = lettersOfVoting.get(0);
        boolean isLink = requestLetter.getAttention()==Attention.LINK;
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(requestLetter.getLocalisation());
        String subject = transcripter.getTextSubject(Subject.END_VOTING);
        for (AloneNew forLinking :
                lettersOfVoting) {
            if (forLinking == null || forLinking.getTextInfo() == null)
                throw new RuntimeException("letter(s) of voting not found");
            Set<Recipient> recipients = forLinking.getSendTo();
            Set<Recipient> inHolder = new HashSet<>();
            for (Recipient guard :
                    recipients) {
                if (standardInfoHolder.getOnlineInfo().get(guard.getExternUuid()) != null) {
//                    standardInfoHolder.removeMessageFromPerson(guard.getExternUuid(),forLinking);
                    standardInfoHolder.getOnlineInfo().get(guard.getExternUuid()).removeNewMessage(aloneNewMapper.entityToDto(forLinking));
                    inHolder.add(guard);
                }
            }
            forLinking.setNotReadingString(forLinking.getSendTo().stream().map(Recipient::getExternUuid).collect(Collectors.joining(" ")));
            forLinking.setCreationDate(new Timestamp(System.currentTimeMillis()));
            forLinking.setAlreadyRead(false);
            forLinking.setPostProcess(true);
            forLinking.setSubject(subject);
            LetterLocal reserveLocal = forLinking.getLetterLocalisations().get(0);
            reserveLocal.setSubject(Subject.END_VOTING.name());
            forLinking.setTextInfo(localisationService.generateTextOfLetter(reserveLocal.getTextInfo(), transcripter, Subject.END_VOTING, forLinking.getAttention()));
            if (!inHolder.isEmpty()) for (Recipient guard :
                    inHolder) {
                standardInfoHolder.addNewMessageToPerson(localisationService.getLetterDtoTo(forLinking, guard, null, true).orElseThrow(), guard.getExternUuid());
            }
        }
        DirectiveGuards directive = DirectiveGuards.builder()
                .person(requestLetter.getExternId())
                .build();
        if (voting.getAccepts() >= voting.getRejects()) {
            if (isLink) directive.setOperation(KafkaOperation.EDIT);
            else directive.setOperation(KafkaOperation.ADD);
        } else {
            if (isLink) directive.setOperation(KafkaOperation.RENAME);
            else directive.setOperation(KafkaOperation.REMOVE);

        }
        votingRepo.delete(voting);
        votingDirective.add(directive);
    }
}
