package com.example.service;

import com.example.dtos.FamilyDirective;
import com.example.dtos.TokenUser;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.holders.StandardInfoHolder;
import com.example.mappers.AloneNewMapper;
import com.example.models.StandardInfo;
import com.example.repository.RecipientRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipientService {
    private RecipientRepo recipientRepo;
    private StandardInfoHolder standardInfoHolder;
    private AloneNewMapper letterMapper;

    @Transactional(readOnly = true)
    public Recipient findRecipient(String externId) {
        return recipientRepo.findByExternId(externId);
    }

    @Transactional(readOnly = true)
    public Optional<Recipient> findRecipient(Long Id) {
        return recipientRepo.findById(Id);
    }

    @Transactional(readOnly = true)
    public Recipient getRecipient(TokenUser tokenUser) {
        Recipient recipient = findRecipient((String) tokenUser.getClaims().get("sub"));
        return (recipient != null) ? recipient : creatRecipient(tokenUser);
    }

    @Transactional
    public Recipient creatRecipient(TokenUser tokenUser) {
        Recipient recipient = Recipient.builder()
                .nickName(tokenUser.getNickName())
                .externId((String) tokenUser.getClaims().get("sub"))
                .commonReading("")
                .systemReading("")
                .build();
        if (tokenUser.getEmail() != null) recipient.setEmail(tokenUser.getEmail());
        recipientRepo.save(recipient);
        return recipient;
    }

    @Transactional
    public Recipient creatRecipient(FamilyDirective directive) {
        Recipient recipient = Recipient.builder()
                .nickName(directive.getPerson())
                .externId(directive.getTokenUser())
                .commonReading("")
                .systemReading("")
                .build();
        recipientRepo.save(recipient);
        return recipient;
    }

    @Transactional
    public void saveRecipient(Recipient recipient) {
        recipientRepo.save(recipient);
    }

    @Transactional
    public void inlineProcess(FamilyDirective directive) {
        Recipient recipient = findRecipient(directive.getTokenUser());
        StandardInfo standardInfo = new StandardInfo();

        if (recipient == null) {
            creatRecipient(directive);
            standardInfo.getSystemGlobalRead().addAll(standardInfoHolder.getSystemGlobalMask());
            standardInfo.getCommonGlobalRead().addAll(standardInfoHolder.getCommonGlobalMask());
            standardInfoHolder.getOnlineInfo().put(directive.getTokenUser(), standardInfo);
        } else {
            List<AloneNew> letters = recipient.getReceivedLetters();
            for (AloneNew letter :
                    letters) {
                if (!letter.isAlreadyRead()) standardInfo.addNewMessageToPerson(letterMapper.entityToDto(letter));
            }
            standardInfo.getSystemGlobalRead().addAll(recipient.getSystemReading().chars().map(x -> x - 48).boxed().toList());
//            int oldSystem = standardInfo.getSystemGlobalRead().size();
//            StringBuilder stringBuffer = new StringBuilder(recipient.getSystemReading());
//            for (int i = 1; i <= (standardInfoHolder.getSystemNewsGlobal().size() - oldSystem); i++) {
//                standardInfo.getSystemGlobalRead().add(0);
//                stringBuffer.append(0);
//            }
//            recipient.setSystemReading(stringBuffer.toString());
//
//            standardInfo.getCommonGlobalRead().addAll(recipient.getCommonReading().chars().map(x -> x - 48).boxed().toList());
//            int oldCommon = standardInfo.getCommonGlobalRead().size();
//            StringBuilder stringBuffer1 = new StringBuilder(recipient.getCommonReading());
//            for (int i = 1; i <= (standardInfoHolder.getCommonNewsGlobal().size() - oldCommon); i++) {
//                standardInfo.getCommonGlobalRead().add(0);
//                stringBuffer1.append(0);
//            }
//            recipient.setCommonReading(stringBuffer1.toString());
            standardInfoHolder.getOnlineInfo().put(directive.getTokenUser(), standardInfo);
            if (!recipient.getNickName().equals(directive.getPerson())) {
                recipient.setNickName(directive.getPerson());
                recipientRepo.save(recipient);
            }
        }

    }
}

