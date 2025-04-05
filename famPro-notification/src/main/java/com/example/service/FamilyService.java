package com.example.service;

import com.example.dtos.DirectiveGuards;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.entity.Voting;
import com.example.enums.KafkaOperation;
import com.example.enums.NewsCategory;
import com.example.repository.AloneNewRepo;
import com.example.repository.NotificationRepo;
import com.example.repository.VotingRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
@Service
@Log4j2
public class FamilyService {
    private RecipientService recipientService;
    private AloneNewRepo aloneNewRepo;
    private NotificationRepo notificationRepo;
    private VotingRepo votingRepo;


    @Transactional
    public AloneNew formMessageToGuards(DirectiveGuards directiveGuards) {
        Set<Recipient> guards = notificationRepo.findGuards(directiveGuards.getGuards());
        AloneNew aloneNew;
        if (directiveGuards.getInfo2() != null)
            aloneNew = aloneNewRepo.save(AloneNew.builder()
                    .category(NewsCategory.FAMILY)
                    .alreadyRead(false)
                    .externId(directiveGuards.getId())
                    .creationDate(directiveGuards.getCreated())
                    .textInfo(StringUtils.join("Запрашивается подтверждение наличия связи:", textLinkRequest(directiveGuards),
                            "<br>",
                            "ВАШ ВЫБОР?", " "))
                    .sendTo(guards)
                    .subject("Link request")
                    .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                    .build());
        else aloneNew = aloneNewRepo.save(AloneNew.builder()
                .category(NewsCategory.FAMILY)
                .alreadyRead(false)
                .externId(directiveGuards.getId())
                        .imageUrl(directiveGuards.getPerson())
                .creationDate(directiveGuards.getCreated())
                .textInfo(directiveGuards.getInfo1())
                .sendTo(guards)
                .subject("Linking to person")
                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .build());
        votingRepo.save(Voting.builder()
                .recipients(guards)
                .letter(aloneNew.getExternId())
                .guardSize(directiveGuards.getGuards().size())
                .accepts(0)
                .rejects(0)
                .build());
        return aloneNew;
    }

    private String textLinkRequest(DirectiveGuards directiveGuards) {
        return StringUtils.join(
                "<br>",
                directiveGuards.getInfo1(),
                "<br>",
                directiveGuards.getSwitchPosition().getInfo(),
                "<br>",
                directiveGuards.getInfo2(),
                "<br>",
                "Это приведет к слиянию глобальных семей численностью:",
                directiveGuards.getGlobalNumber1(),
                "и",
                directiveGuards.getGlobalNumber2(),
                "<br>",
                "ИДЕТ ГОЛОСОВАНИЕ");
    }

    @Transactional
    public AloneNew formMessageToRequester(DirectiveGuards directiveGuards) {
        if (directiveGuards.getInfo2() != null)
            return aloneNewRepo.save(AloneNew.builder()
                    .category(NewsCategory.FAMILY)
                    .externId(directiveGuards.getId())
                    .alreadyRead(false)
                    .creationDate(directiveGuards.getCreated())
                    .textInfo(StringUtils.join("Вы запросили связь:", textLinkRequest(directiveGuards), " "))
                    .subject("Request link")
                    .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
                    .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                    .build());
        else return aloneNewRepo.save(AloneNew.builder()
                .category(NewsCategory.FAMILY)
                .externId(directiveGuards.getId())
                .alreadyRead(false)
                .creationDate(directiveGuards.getCreated())
                .textInfo(directiveGuards.getInfo1())
                .subject("Link to person")
                .imageUrl(String.valueOf(directiveGuards.getGlobalNumber2()))
                .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .build());
    }

    @Transactional
    public AloneNew formAnswerToRequester(DirectiveGuards directiveGuards) {
        if (directiveGuards.getOperation() == KafkaOperation.ADD) {
            Recipient requester = recipientService.findRecipient(directiveGuards.getTokenUser());
            requester.setUrlPhoto(directiveGuards.getGlobalNumber2() == 1);
            requester.setLinkExternId(directiveGuards.getPerson());
            recipientService.getRecipientRepo().save(requester);
            return aloneNewRepo.save(AloneNew.builder()
                    .category(NewsCategory.FAMILY)
                    .externId(directiveGuards.getId())
                    .alreadyRead(false)
                    .creationDate(directiveGuards.getCreated())
                    .textInfo(directiveGuards.getInfo1())
                    .subject("Accept change")
                    .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
                    .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                    .build());
        }
        return aloneNewRepo.save(AloneNew.builder()
                .category(NewsCategory.FAMILY)
                .externId(directiveGuards.getId())
                .alreadyRead(false)
                .creationDate(directiveGuards.getCreated())
                .textInfo(StringUtils.join("Ваш запрос на изменение ", directiveGuards.getInfo2(), " отклонен в связи с:", directiveGuards.getInfo1(), " "))
                .subject("Reject change")
                .sendTo(Collections.singleton(recipientService.findRecipient(directiveGuards.getTokenUser())))
                .sendFrom(recipientService.findRecipient(3L).orElseThrow(() -> new RuntimeException("Informer recipient not found")))
                .build());
    }
}
