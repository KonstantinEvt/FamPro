package com.example.service;

import com.example.dtos.AloneNewDto;
import com.example.dtos.DirectiveGuards;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.entity.Voting;
import com.example.enums.Attention;
import com.example.enums.KafkaOperation;
import com.example.enums.NewsCategory;
import com.example.enums.SwitchPosition;
import com.example.holders.StandardInfoHolder;
import com.example.mappers.AloneNewMapper;
import com.example.repository.AloneNewRepo;
import com.example.repository.NotificationRepo;
import com.example.repository.VotingRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Service
@Log4j2
public class ReceiveAndFormService {
    private RecipientService recipientService;
    private AloneNewRepo aloneNewRepo;
    private NotificationRepo notificationRepo;
    private VotingRepo votingRepo;
    private AloneNewMapper aloneNewMapper;
    private StandardInfoHolder standardInfoHolder;

    public void putLetterToHolder(AloneNew letter, String sendingTo) {
        AloneNewDto letterForRequester = aloneNewMapper.entityToDto(letter);
        letterForRequester.setSendingTo(sendingTo);
        letterForRequester.setSendingFromAlt(letter.getSendFrom().getNickName());
        standardInfoHolder.addNewMessageToPerson(letterForRequester);
    }

    @Transactional
    public void receiveAttentionLetter(DirectiveGuards directiveGuards) {
        Recipient recipient = recipientService.findRecipient(directiveGuards.getTokenUser());
        AloneNew aloneNew = formAnswerByInformer(directiveGuards, NewsCategory.PRIVATE, recipient);
        putLetterToHolder(aloneNew, directiveGuards.getTokenUser());
    }

    @Transactional
    public void receiveVotingLetter(DirectiveGuards directiveGuards) {
        if (directiveGuards.getGuards() == null || directiveGuards.getGuards().isEmpty())
            throw new RuntimeException("wrong directive");
        Recipient recipient = recipientService.findRecipient(directiveGuards.getTokenUser());
        directiveGuards.setInfo2(directiveGuards.getInfo2().concat(recipient.getNickName()));
        AloneNew aloneNewGuards = formMessageToGuards(directiveGuards);
        AloneNewDto letterForGuards = aloneNewMapper.entityToDto(aloneNewGuards);
        letterForGuards.setSendingFromAlt(aloneNewGuards.getSendFrom().getNickName());
        for (String guard : directiveGuards.getGuards()) {
            letterForGuards.setSendingTo(guard);
            standardInfoHolder.addNewMessageToPerson(letterForGuards);
        }
        directiveGuards.setSwitchPosition(SwitchPosition.BURIAL);
        directiveGuards.setInfo1(null);
        AloneNew aloneNewRequester = formAnswerByInformer(directiveGuards, NewsCategory.FAMILY, recipient);
        putLetterToHolder(aloneNewRequester, directiveGuards.getTokenUser());
    }

    @Transactional
    public AloneNew formMessageToGuards(DirectiveGuards directiveGuards) {
        Set<Recipient> guards = notificationRepo.findGuards(directiveGuards.getGuards());
        AloneNew aloneNew = AloneNew.builder()
                .category(NewsCategory.FAMILY)
                .alreadyRead(false)
                .externId(directiveGuards.getId())
                .subject(directiveGuards.getInfo1())
                .textInfo(directiveGuards.getInfo2())
                .creationDate(directiveGuards.getCreated())
                .sendTo(guards)
                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .localisation(directiveGuards.getLocalisation())
                .build();
        if (directiveGuards.getSwitchPosition() == SwitchPosition.BIRTH) aloneNew.setAttention(Attention.LINK);
        else aloneNew.setAttention(Attention.VOTING);
        aloneNewRepo.save(aloneNew);

        votingRepo.save(Voting.builder()
                .recipients(guards)
                .letter(aloneNew.getExternId())
                .guardSize(directiveGuards.getGuards().size())
                .accepts(0)
                .rejects(0)
                .build());
        return aloneNew;
    }

    @Transactional
    public AloneNew formAnswerByInformer(DirectiveGuards directiveGuards, NewsCategory category, Recipient recipient) {
        String info = directiveGuards.getInfo2();
        if (directiveGuards.getInfo1() != null && !directiveGuards.getInfo1().isBlank()) {
            info = info.concat("<br>").concat(directiveGuards.getInfo1());
        }
        if (directiveGuards.getNumber1() != 0L)
            info = info.concat("<br>").concat(String.valueOf(directiveGuards.getNumber1()));

        AloneNew aloneNew = AloneNew.builder()
                .externId(directiveGuards.getId())
                .alreadyRead(false)
                .subject(directiveGuards.getInfo3())
                .creationDate(directiveGuards.getCreated())
                .textInfo(info)
                .sendTo(Collections.singleton(recipient))
                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .localisation(directiveGuards.getLocalisation())
                .build();

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
                    recipientService.changeRecipient(directiveGuards);
                } else {
                    aloneNew.setAttention(Attention.NEGATIVE);
                }
            }
            case BIRTH -> {
                aloneNew.setAttention(Attention.POSITIVE);
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
