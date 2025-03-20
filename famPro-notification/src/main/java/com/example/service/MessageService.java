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
import com.example.repository.NotificationRepo;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class MessageService {
    private StandardInfoHolder infoHolder;
    private AloneNewMapper aloneNewMapper;
    private AloneNewRepo aloneNewRepo;
    private RecipientService recipientService;
    private NotificationRepo notificationRepo;

    @Transactional
    public void sendMessage(TokenUser tokenUser, AloneNewDto aloneNewDto) {
        aloneNewDto.setCreationDate(new Timestamp(System.currentTimeMillis()));
        aloneNewDto.setSendingFrom((String) tokenUser.getClaims().get("sub"));
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
        log.info("New letter is saved");
        aloneNewDto.setId(aloneNew.getId());
        aloneNewDto.setSendingFromAlt(from.getNickName());
        switch (aloneNew.getCategory()) {
            case SYSTEM -> {
                if (to == null) {
                    aloneNewDto.setSendingFrom("s".concat(String.valueOf(infoHolder.getSystemNewsGlobal().size())));
                    infoHolder.getSystemNewsGlobal().add(aloneNewDto);
                    infoHolder.getSystemGlobalMask().add(0);
                } else {
                    aloneNewDto.setSendingFrom(String.valueOf(aloneNew.getId()));
                    infoHolder.addNewMessageToPerson(aloneNewDto);
                }
            }
            case COMMON -> {
                aloneNewDto.setSendingFrom("c".concat(String.valueOf(infoHolder.getCommonNewsGlobal().size())));
                infoHolder.getCommonNewsGlobal().add(aloneNewDto);
                infoHolder.getCommonGlobalMask().add(0);
            }
            default -> {
                if (to == null) throw new RuntimeException("Адресат не указан");
//                if (!to.getContacts().contains(from)) aloneNewDto.setSendingFrom(tokenUser.getNickName());
                if (infoHolder.getOnlineInfo().containsKey(to.getExternId()))
                    infoHolder.addNewMessageToPerson(aloneNewDto);
            }
        }
        log.info("letter ${} is entered to holder", aloneNewDto);
    }

    public int[] getNewsCounts(String user) {
        StandardInfo userInfo = infoHolder.getOnlineInfo().get(user);
        if (userInfo == null)
            return new int[]{0, 0, 0, 0, 0};
        System.out.println("hi");
        int com = userInfo.viewGlobalMessage(infoHolder.getCommonGlobalMask(), userInfo.getCommonGlobalRead());
        int sys = userInfo.viewGlobalMessage(infoHolder.getSystemGlobalMask(), userInfo.getSystemGlobalRead());
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
        System.out.println(id);
        Recipient recipient = recipientService.findRecipient(user);
        if (id.charAt(0) == 's') {
            int element = Integer.parseInt(id.substring(1));
            List<Integer> reading = infoHolder.getOnlineInfo().get(user).getSystemGlobalRead();
            reading.set(element, 1);
            recipient.setSystemReading(StringUtils.join(reading, ""));
        } else if (id.charAt(0) == 'c') {
            int element = Integer.parseInt(id.substring(1));
            List<Integer> reading = infoHolder.getOnlineInfo().get(user).getCommonGlobalRead();
            reading.set(element, 1);
            recipient.setCommonReading(StringUtils.join(reading, ""));
        } else {
            System.out.println(id);
            readIndividualMessage(user, UUID.fromString(id));
        }
        recipientService.saveRecipient(recipient);
    }

    @Transactional
    public void readIndividualMessage(String user, UUID id) {
        AloneNew letter = aloneNewRepo.findById(id).orElseThrow(() -> new RuntimeException("Letter not found"));
        if (infoHolder.getOnlineInfo().get(user) != null)
            infoHolder.getOnlineInfo().get(user).removeNew(aloneNewMapper.entityToDto(letter));
        letter.setAlreadyRead(true);
        aloneNewRepo.save(letter);
    }

    public void removeIndividualMessage(String user, UUID id) {
        AloneNew letter = aloneNewRepo.findById(id).orElseThrow(() -> new RuntimeException("Letter not found"));
        if (infoHolder.getOnlineInfo().get(user) != null)
            infoHolder.getOnlineInfo().get(user).removeNew(aloneNewMapper.entityToDto(letter));
        aloneNewRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AloneNewDto> getAllNewsByCategory(String user, NewsCategory category) {
        Recipient recipient = notificationRepo.findRecipientWithReceivedLetter(user);
        if (recipient != null && !recipient.getReceivedLetters().isEmpty()) {
            List<AloneNew> aloneNewList = notificationRepo.getLettersWithSendersByCategory(recipient.getReceivedLetters(), category);
            if (aloneNewList.isEmpty()) return new ArrayList<>();
            List<AloneNewDto> resultList = new ArrayList<>();
            for (AloneNew letter :
                    aloneNewList) {
                AloneNewDto aloneNewDto = aloneNewMapper.entityToDto(letter);
                if (letter.getSendFrom() != null) {
                    aloneNewDto.setSendingFromAlt(letter.getSendFrom().getNickName());
                    resultList.add(aloneNewDto);
                }
            }
            return resultList;
        } else return new ArrayList<>();
    }

    @Transactional
    public String acceptForMessage(String user, UUID uuid) {


        return "Accept is counting";
    }

    @Transactional
    public String rejectForMessage(String user, UUID uuid) {
        return "Reject is counting";
    }

    @Transactional
    public void removeRecipientFromSendTo(String user, String externId) {
        List<AloneNew> aloneNews = notificationRepo.getAloneNewWithSendTo(externId);
        Recipient recipient = recipientService.findRecipient(user);
        AloneNew needAlone = null;
        for (AloneNew aloneNew :
                aloneNews) {
            if (aloneNew != null && !aloneNew.getSendTo().isEmpty() && aloneNew.getSendTo().contains(recipient))
                needAlone = aloneNew;
        }
        if (needAlone != null) {
            needAlone.getSendTo().remove(recipient);
            infoHolder.removeMessageFromPerson(user, aloneNewMapper.entityToDto(needAlone));
            if (needAlone.getSendTo().isEmpty() && (needAlone.getSubject().equals("Request link")||needAlone.getSubject().equals("Link to person"))) {
                needAlone.getSendTo().add(recipient);
                needAlone.setAlreadyRead(true);
            }
            if (needAlone.getSendTo().isEmpty()) aloneNewRepo.delete(needAlone);
            else aloneNewRepo.save(needAlone);
        }
    }
}
