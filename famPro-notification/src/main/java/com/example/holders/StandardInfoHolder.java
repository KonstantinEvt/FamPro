package com.example.holders;

import com.example.dtos.AloneNewDto;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.enums.Attention;
import com.example.mappers.AloneNewMapper;
import com.example.models.StandardInfo;
import com.example.repository.RecipientRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Getter
@Setter
public class StandardInfoHolder {
    private final AloneNewMapper aloneNewMapper;
    private final RecipientRepository recipientRepository;


    private final Map<String, StandardInfo> onlineInfo;
    private final List<AloneNewDto> systemNewsGlobal;
    private final List<AloneNewDto> commonNewsGlobal;
    private final List<Integer> systemGlobalMask;
    private final List<Integer> commonGlobalMask;

    public StandardInfoHolder(
            AloneNewMapper aloneNewMapper,
            RecipientRepository recipientRepository,
            Map<String, StandardInfo> onlineInfo,
            List<AloneNewDto> systemNewsGlobal,
            List<AloneNewDto> commonNewsGlobal,
            List<Integer> systemGlobalMask,
            List<Integer> commonGlobalMask) {
        this.aloneNewMapper = aloneNewMapper;
        this.recipientRepository = recipientRepository;
        this.onlineInfo = onlineInfo;
        this.systemNewsGlobal = systemNewsGlobal;
        this.commonNewsGlobal = commonNewsGlobal;
        this.systemGlobalMask = systemGlobalMask;
        this.commonGlobalMask = commonGlobalMask;
    }

    @PostConstruct
    @Transactional
    public void setUpMainRecipients() {
        Optional<Recipient> systemRecipient = recipientRepository.getRecipientWithReceiveLettersById(1L);
        Optional<Recipient> commonRecipient = recipientRepository.getRecipientWithReceiveLettersById(2L);
        if (systemRecipient.isPresent() && commonRecipient.isPresent()) {
            systemNewsGlobal.addAll(systemRecipient.get().getReceivedLetters()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(x -> Objects.equals(x.getAttention(), Attention.SYSTEM))
                    .sorted(Comparator.comparing(AloneNew::getCreationDate))
                    .map(aloneNewMapper::entityToDto)
                    .toList());
            for (int i = 0; i < systemNewsGlobal.size(); i++) {
                systemNewsGlobal.get(i).setSendingFrom(String.valueOf(i));
            }
            systemGlobalMask.addAll(systemRecipient.get().getSystemReading().chars().map(x -> x - 48).boxed().toList());
            commonNewsGlobal.addAll(commonRecipient.get().getReceivedLetters()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(x -> Objects.equals(x.getAttention(), Attention.COMMON))
                    .sorted(Comparator.comparing(AloneNew::getCreationDate))
                    .map(aloneNewMapper::entityToDto)
                    .toList());
            for (int i = 0; i < commonNewsGlobal.size(); i++) {
                commonNewsGlobal.get(i).setSendingFrom(String.valueOf(i));
            }
            commonGlobalMask.addAll(commonRecipient.get().getCommonReading().chars().map(x -> x - 48).boxed().toList());
        } else {
            recipientRepository.persistNewRecipient(Recipient.builder()
                    .nickName("System")
                    .systemReading("")
                    .externUuid(UUID.nameUUIDFromBytes("SYSTEM_RECIPIENT".getBytes()).toString())
                    .build());
            recipientRepository.persistNewRecipient(Recipient.builder()
                    .nickName("Common")
                    .commonReading("")
                    .externUuid(UUID.nameUUIDFromBytes("COMMON_RECIPIENT".getBytes()).toString())
                    .build());
            recipientRepository.persistNewRecipient(Recipient.builder()
                    .nickName("Informer")
                    .externUuid(UUID.nameUUIDFromBytes("INFORMER_RECIPIENT".getBytes()).toString())
                    .build());
        }
    }

    public void addNewMessageToPerson(AloneNewDto aloneNewDto) {
        String onlineUser = aloneNewDto.getSendingTo();
        if (onlineInfo.containsKey(onlineUser)) onlineInfo.get(onlineUser).addNewMessage(aloneNewDto);
    }

    public void removeMessageFromPerson(String user, AloneNewDto aloneNewDto) {
        System.out.println("delete from holder");
        if (onlineInfo.containsKey(user)) onlineInfo.get(user).removeNewMessage(aloneNewDto);
    }

    public List<AloneNewDto> getGlobalSystemNewsOfPerson(String user, boolean all) {
        List<AloneNewDto> rezult = new ArrayList<>();
        StandardInfo standardInfo = onlineInfo.get(user);
        for (int i = 0; i < systemNewsGlobal.size(); i++) {
            if (i == standardInfo.getSystemGlobalRead().size()) standardInfo.getSystemGlobalRead().add(0);
            int localMask = systemGlobalMask.get(i) + standardInfo.getSystemGlobalRead().get(i);
            if (!all && localMask == 0 || all && localMask < 3)
                rezult.add(systemNewsGlobal.get(i));
        }
        return rezult;
    }

    public List<AloneNewDto> getCommonNewsOfPerson(String user, boolean all) {
        List<AloneNewDto> rezult = new ArrayList<>();
        StandardInfo standardInfo = onlineInfo.get(user);
        for (int i = 0; i < commonNewsGlobal.size(); i++) {
            if (i == standardInfo.getCommonGlobalRead().size()) standardInfo.getCommonGlobalRead().add(0);
            int localMask = commonGlobalMask.get(i) + standardInfo.getCommonGlobalRead().get(i);
            if (!all && localMask == 0 || all && localMask < 3)
                rezult.add(commonNewsGlobal.get(i));
        }
        return rezult;
    }
}
