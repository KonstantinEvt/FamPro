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
        AloneNew aloneNew = formAnswerByInformer(directiveGuards, NewsCategory.PRIVATE);
        putLetterToHolder(aloneNew, directiveGuards.getTokenUser());
    }

    @Transactional
    public void receiveVotingLetter(DirectiveGuards directiveGuards) {
        if (directiveGuards.getGuards() == null || directiveGuards.getGuards().isEmpty())
            throw new RuntimeException("wrong directive");

        AloneNew aloneNewGuards = formMessageToGuards(directiveGuards);
        AloneNewDto letterForGuards = aloneNewMapper.entityToDto(aloneNewGuards);
        letterForGuards.setSendingFromAlt(aloneNewGuards.getSendFrom().getNickName());
        for (String guard : directiveGuards.getGuards()) {
            letterForGuards.setSendingTo(guard);
            standardInfoHolder.addNewMessageToPerson(letterForGuards);
        }
        directiveGuards.setSwitchPosition(SwitchPosition.BURIAL);
        directiveGuards.setInfo3("Voting");
        AloneNew aloneNewRequester = formAnswerByInformer(directiveGuards, NewsCategory.FAMILY);
        putLetterToHolder(aloneNewRequester, directiveGuards.getTokenUser());
    }

    @Transactional
    public AloneNew formMessageToGuards(DirectiveGuards directiveGuards) {
        Set<Recipient> guards = notificationRepo.findGuards(directiveGuards.getGuards());
        AloneNew aloneNew = AloneNew.builder()
                .category(NewsCategory.FAMILY)
                .alreadyRead(false)
                .externId(directiveGuards.getId())
                .textInfo(formTextFromGuardsDirective(directiveGuards))
                .attention(Attention.VOTING)
                .creationDate(directiveGuards.getCreated())
                .sendTo(guards)
                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .build();

        switch (directiveGuards.getSwitchPosition()) {
            case MAIN -> {
                aloneNew.setSubject("New child in family");
            }
            case FATHER, MOTHER -> {
                aloneNew.setSubject("Child is found");
            }
            case CHILD -> {
                aloneNew.setSubject("Parent is found");
            }
            case BIRTH -> {
                aloneNew.setSubject("User want to be linking");
            }
            default -> {
                aloneNew.setSubject("Something happened");
            }
        }
        aloneNewRepo.save(aloneNew);
//        if (directiveGuards.getInfo2() != null)
//            aloneNew = aloneNewRepo.save();
//        else aloneNew = aloneNewRepo.save(AloneNew.builder()
//                .category(NewsCategory.FAMILY)
//                .alreadyRead(false)
//                .externId(directiveGuards.getId())
//                .imageUrl(directiveGuards.getPerson())
//                .creationDate(directiveGuards.getCreated())
//                .textInfo(directiveGuards.getInfo1())
//                .sendTo(guards)
//                .subject("Linking to person")
//                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
//                .build());

        votingRepo.save(Voting.builder()
                .recipients(guards)
                .letter(aloneNew.getExternId())
                .guardSize(directiveGuards.getGuards().size())
                .accepts(0)
                .rejects(0)
                .build());
        return aloneNew;
    }

    private String formTextFromGuardsDirective(DirectiveGuards directiveGuards) {
        return StringUtils.join(
                "<br>",
                directiveGuards.getInfo1(),
                "<br>",
                "родственная связь родитель-ребенок",
                "<br>",
                directiveGuards.getInfo2(),
                "<br>",
                "ИДЕТ ГОЛОСОВАНИЕ");
    }

//    @Transactional
//    public AloneNew formMessageToRequester(DirectiveGuards directiveGuards) {
//        if (directiveGuards.getInfo2() != null)
//            return aloneNewRepo.save(AloneNew.builder()
//                    .category(NewsCategory.FAMILY)
//                    .externId(directiveGuards.getId())
//                    .alreadyRead(false)
//                    .creationDate(directiveGuards.getCreated())
//                    .textInfo(StringUtils.join("Вы запросили связь:", formTextFromGuardsDirective(directiveGuards), " "))
//                    .subject("Request link")
//                    .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
//                    .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
//                    .build());
//        else return aloneNewRepo.save(AloneNew.builder()
//                .category(NewsCategory.FAMILY)
//                .externId(directiveGuards.getId())
//                .alreadyRead(false)
//                .creationDate(directiveGuards.getCreated())
//                .textInfo(directiveGuards.getInfo1())
//                .subject("Link to person")
//                .imageUrl(String.valueOf(directiveGuards.getGlobalNumber2()))
//                .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
//                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
//                .build());
//    }

    @Transactional
    public AloneNew formAnswerByInformer(DirectiveGuards directiveGuards, NewsCategory category) {
        String info = null;
        if (directiveGuards.getInfo1() != null && !directiveGuards.getInfo1().isBlank()) {
            info = directiveGuards.getInfo1();
            if (directiveGuards.getInfo2() != null && !directiveGuards.getInfo2().isBlank()) {
                info = StringUtils.join(info, "<br>", directiveGuards.getInfo2());
                if (directiveGuards.getInfo3() != null && !directiveGuards.getInfo3().isBlank()) {
                    info = StringUtils.join(info, "<br>", directiveGuards.getInfo3());
                }
            }
        }
        AloneNew aloneNew = AloneNew.builder()
                .externId(directiveGuards.getId())
                .alreadyRead(false)
                .creationDate(directiveGuards.getCreated())
                .textInfo(info)
                .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .build();

        switch (directiveGuards.getSwitchPosition()) {
            case MAIN -> {
                aloneNew.setAttention(Attention.MODERATE);
                aloneNew.setSubject("Moderation warning");
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
            case FATHER -> {
                aloneNew.setAttention(Attention.RIGHTS);
                aloneNew.setSubject("Rights warning");
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
            case MOTHER -> {
                aloneNew.setAttention(Attention.NEGATIVE);
                aloneNew.setSubject("Reject change");
                aloneNew.setCategory(Objects.requireNonNullElse(category, NewsCategory.PRIVATE));
            }
            case PRIME -> {
                aloneNew.setAttention(Attention.LINK);
                aloneNew.setCategory(NewsCategory.PRIVATE);
                if (directiveGuards.getPerson() != null) {

                    aloneNew.setSubject("Accept link");
                    recipientService.changeRecipient(directiveGuards);
                } else {
                    aloneNew.setSubject("Reject link");

                }
            }
            case BIRTH -> {
                aloneNew.setAttention(Attention.POSITIVE);
                aloneNew.setSubject("Accept change");
                aloneNew.setCategory(Objects.requireNonNullElse(category, NewsCategory.PRIVATE));
            }
            case ADDRESS -> {
                aloneNew.setAttention(Attention.DATE);
                aloneNew.setSubject("Object change between requests");
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
            case BURIAL -> {
                aloneNew.setAttention(Attention.VOTING_REQUESTER);
                aloneNew.setSubject(directiveGuards.getInfo3());
                aloneNew.setCategory(NewsCategory.PRIVATE);
                aloneNew.setTextInfo(formTextFromGuardsDirective(directiveGuards));
            }
            default -> {
                aloneNew.setAttention(Attention.UNKNOWN);
                aloneNew.setSubject("Unknown action");
                aloneNew.setCategory(NewsCategory.PRIVATE);
            }
        }
if (directiveGuards.getSubject()!=null) aloneNew.setSubject(directiveGuards.getSubject().getRus());
        return aloneNewRepo.save(aloneNew);

//        return aloneNewRepo.save(AloneNew.builder()
//                .category(NewsCategory.FAMILY)
//                .externId(directiveGuards.getId())
//                .alreadyRead(false)
//                .creationDate(directiveGuards.getCreated())
//                .textInfo(StringUtils.join("Ваш запрос на изменение ", directiveGuards.getInfo2(), " отклонен в связи с:", directiveGuards.getInfo1(), " "))
//                .subject("Reject change")
//                .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
//                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
//                .build());
    }
}
