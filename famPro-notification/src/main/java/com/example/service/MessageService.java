package com.example.service;

import com.example.dtos.AloneNewDto;
import com.example.dtos.TokenUser;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.enums.NewsCategory;
import com.example.holders.StandardInfoHolder;
import com.example.mappers.AloneNewMapper;
import com.example.models.StandardInfo;
import com.example.repository.AloneNewRepo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private StandardInfoHolder infoHolder;
    private AloneNewMapper aloneNewMapper;
    private AloneNewRepo aloneNewRepo;
    private RecipientService recipientService;

    @Transactional
    public void sendMessage(TokenUser tokenUser, AloneNewDto aloneNewDto) {
        aloneNewDto.setCreationDate(new Timestamp(System.currentTimeMillis()));
        aloneNewDto.setSendingFrom(tokenUser.getNickName());
        Recipient from = recipientService.getRecipient(tokenUser);
        Recipient to = null;
        if (aloneNewDto.getSendingTo() != null && !aloneNewDto.getSendingTo().isBlank()) {
            to = recipientService.findRecipient(aloneNewDto.getSendingTo());
            if (to == null) throw new RuntimeException("Адресат не найден");
        }
        AloneNew aloneNew = aloneNewMapper.dtoToEntity(aloneNewDto);
        if (aloneNew.getId() != null) aloneNew.setId(null);
        aloneNew.setSendFrom(from);
        switch (aloneNew.getCategory()) {
            case SYSTEM -> {
                Recipient sysRecipient = recipientService.findRecipient(1L).orElseThrow(() -> new RuntimeException("SystemRecipient not found"));
                if (to == null) {
                    aloneNew.setSendTo(Collections.singleton(sysRecipient));
                    sysRecipient.setSystemReading(sysRecipient.getSystemReading().concat(String.valueOf(0)));
                } else {
                    aloneNew.setSendFrom(sysRecipient);
                    aloneNew.setSendTo(Collections.singleton(to));
                }
            }
            case COMMON -> {
                Recipient commonRecipient = recipientService.findRecipient(2L).orElseThrow(() -> new RuntimeException("CommonRecipient not found"));
                aloneNew.setSendTo(Collections.singleton(commonRecipient));
                commonRecipient.setCommonReading(commonRecipient.getCommonReading().concat(String.valueOf(0)));
            }
            default -> aloneNew.setSendTo(Collections.singleton(to));
        }
        aloneNewRepo.save(aloneNew);
        aloneNewDto.setId(aloneNew.getId());
        switch (aloneNew.getCategory()) {
            case SYSTEM -> {
                if (to == null) {
                    aloneNewDto.setSendingFrom("s".concat(String.valueOf(infoHolder.getSystemNewsGlobal().size())));
                    infoHolder.getSystemNewsGlobal().add(aloneNewDto);
                    infoHolder.getSystemGlobalMask().add(0);
                } else infoHolder.addNewMessageToPerson(aloneNewDto);
            }
            case COMMON -> {
                aloneNewDto.setSendingFrom("c".concat(String.valueOf(infoHolder.getCommonNewsGlobal().size())));
                infoHolder.getCommonNewsGlobal().add(aloneNewDto);
                infoHolder.getCommonGlobalMask().add(0);
            }
            default -> {
                if (to == null) throw new RuntimeException("Адресат не указан");
                infoHolder.addNewMessageToPerson(aloneNewDto);
            }
        }

        System.out.println("letter is done");
        System.out.println(aloneNewDto);
    }

    public int[] getNewsCounts(String user) {
        StandardInfo userInfo = infoHolder.getOnlineInfo().get(user);
        if (userInfo == null)
            return new int[]{0, 0, 0, 0, 0};
        System.out.println("hi");
        int com = userInfo.viewGlobalMessage(infoHolder.getCommonGlobalMask(), userInfo.getCommonGlobalRead());
        int sys = userInfo.viewGlobalMessage(infoHolder.getSystemGlobalMask(), userInfo.getSystemGlobalRead());
        System.out.println(infoHolder.getCommonGlobalMask());
        System.out.println(infoHolder.getSystemGlobalMask());
        System.out.println(infoHolder.getSystemNewsGlobal());
        System.out.println(infoHolder.getCommonNewsGlobal());
        System.out.println(Arrays.toString(userInfo.getCounts()));
        return new int[]{
                userInfo.getCounts()[0] + com + sys,
                userInfo.getCounts()[1] + sys,
                com,
                userInfo.getCounts()[3],
                userInfo.getCounts()[4]
        };
    }

    public List<AloneNewDto> getCommonMessages(String user) {
        return infoHolder.getCommonNewsToPerson(user);
    }

    public List<AloneNewDto> getSystemMessages(String user) {
        List<AloneNewDto> global = infoHolder.getGlobalSystemNewsToPerson(user);
        global.addAll(infoHolder.getOnlineInfo().get(user).getSystemNews());
        return global.stream().sorted(Comparator.comparing(AloneNewDto::getCreationDate)).toList();
    }

    @Transactional
    public void readGlobalMessage(String user, String id) {
        Recipient recipient = recipientService.findRecipient(user);
        System.out.println(id);
        int element = Integer.parseInt(id.substring(1));
        if (id.charAt(0) == 's') {
            List<Integer> reading = infoHolder.getOnlineInfo().get(user).getSystemGlobalRead();
            reading.set(element, 1);
            recipient.setSystemReading(StringUtils.join(reading,""));
            System.out.println(element);
            System.out.println(reading);
            System.out.println(recipient.getSystemReading());
        } else if (id.charAt(0) == 'c') {
            List<Integer> reading = infoHolder.getOnlineInfo().get(user).getCommonGlobalRead();
            reading.set(element, 1);
            recipient.setCommonReading(StringUtils.join(reading,""));
        } else readIndividualMessage(user, id);
        recipientService.saveRecipient(recipient);
    }
    @Transactional
    public void readIndividualMessage(String user, String id){
        Recipient recipient = recipientService.findRecipient(user);
        System.out.println(id);
        int element = Integer.parseInt(id.substring(1));
        if (id.charAt(0)=='i') {

       }

    }
}
