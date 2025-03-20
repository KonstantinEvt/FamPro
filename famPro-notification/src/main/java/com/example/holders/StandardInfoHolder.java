package com.example.holders;

import com.example.dtos.AloneNewDto;
import com.example.entity.AloneNew;
import com.example.entity.Recipient;
import com.example.mappers.AloneNewMapper;
import com.example.models.StandardInfo;
import com.example.repository.AloneNewRepo;
import com.example.repository.RecipientRepo;
import com.example.service.TokenService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@AllArgsConstructor
@Getter
@Setter
public class StandardInfoHolder {
    private TokenService tokenService;
    private AloneNewMapper aloneNewMapper;
    private AloneNewRepo aloneNewRepo;
    private RecipientRepo recipientRepo;
    private EntityManager entityManager;

    private Map<String, StandardInfo> onlineInfo;
    private List<AloneNewDto> systemNewsGlobal;
    private List<AloneNewDto> commonNewsGlobal;
    private List<Integer> systemGlobalMask;
    private List<Integer> commonGlobalMask;
    //  private String systemID;

    @PostConstruct
    @Transactional
    public void setUpMainRecipients() {
//        Optional<Recipient> systemRecipient = recipientRepo.findById(1L);
        try {
            Recipient systemRecipient = entityManager.createQuery("select a from Recipient a left join fetch  a.receivedLetters where a.id=1L", Recipient.class).getSingleResult();

            if (systemRecipient.getReceivedLetters() != null) {
                List<AloneNew> systemNews = systemRecipient.getReceivedLetters();
                systemNewsGlobal.addAll(systemNews
                        .stream()
                        .sorted(Comparator.comparing(AloneNew::getCreationDate))
                        .map(x -> aloneNewMapper.entityToDto(x))
                        .toList());
                for (int i = 0; i < systemNewsGlobal.size(); i++) {
                    systemNewsGlobal.get(i).setSendingFrom("s".concat(String.valueOf(i)));
                }
                systemGlobalMask.addAll(systemRecipient.getSystemReading().chars().map(x -> x - 48).boxed().toList());
            }
            Recipient commonRecipient = entityManager.createQuery("select a from Recipient a left join fetch a.receivedLetters where a.id=2L", Recipient.class).getSingleResult();
//            Recipient commonRecipient = recipientRepo.findById(2L).orElseThrow(() -> new RuntimeException("CommonRecipient not found"));
            if (commonRecipient.getReceivedLetters() != null) {
                List<AloneNew> commonNews = commonRecipient.getReceivedLetters();
                commonNewsGlobal.addAll(commonNews
                        .stream()
                        .sorted(Comparator.comparing(AloneNew::getCreationDate))
                        .map(x -> aloneNewMapper.entityToDto(x))
                        .toList());
                for (int i = 0; i < commonNewsGlobal.size(); i++) {
                    commonNewsGlobal.get(i).setSendingFrom("c".concat(String.valueOf(i)));
                }
                commonGlobalMask.addAll(commonRecipient.getCommonReading().chars().map(x -> x - 48).boxed().toList());
            }
        } catch (RuntimeException e) {
            Recipient systemRecipient = Recipient.builder()
                    .nickName("System")
                    .systemReading("")
                    .externId(UUID.nameUUIDFromBytes("SYSTEM_RECIPIENT".getBytes()).toString())
                    .build();
            recipientRepo.save(systemRecipient);
            recipientRepo.save(Recipient.builder()
                    .nickName("Common")
                    .commonReading("")
                    .externId(UUID.nameUUIDFromBytes("COMMON_RECIPIENT".getBytes()).toString())
                    .build());
            Recipient informerRecipient = Recipient.builder()
                    .nickName("Informer")
                    .externId(UUID.nameUUIDFromBytes("INFORMER_RECIPIENT".getBytes()).toString())
                    .build();
            recipientRepo.save(informerRecipient);
        }
    }

    public void addNewMessageToPerson(AloneNewDto aloneNewDto) {
        String onlineUser = aloneNewDto.getSendingTo();
        if (!onlineInfo.containsKey(onlineUser))
            onlineInfo.put(onlineUser, new StandardInfo());
        onlineInfo.get(onlineUser).addNewMessageToPerson(aloneNewDto);
    }
    public void removeMessageFromPerson(String user, AloneNewDto aloneNewDto) {
        System.out.println("delete from holder");
        if (onlineInfo.containsKey(user)) onlineInfo.get(user).removeNew(aloneNewDto);
    }
    public List<AloneNewDto> getGlobalSystemNewsToPerson(String user) {
        List<AloneNewDto> rezult = new ArrayList<>();
        StandardInfo standardInfo = onlineInfo.get(user);
//        int size = standardInfo.getSystemGlobalRead().size();
        for (int i = 0; i < systemNewsGlobal.size(); i++) {
            if (i == standardInfo.getSystemGlobalRead().size()) standardInfo.getSystemGlobalRead().add(0);
            if ((systemGlobalMask.get(i) + standardInfo.getSystemGlobalRead().get(i)) == 0)
                rezult.add(systemNewsGlobal.get(i));
        }
//        if (size != standardInfo.getSystemGlobalRead().size()) {
//            Recipient person = recipientRepo.findByExternId(user);
//            person.setSystemReading(standardInfo.getSystemGlobalRead().toString().replace(" ", ""));
//            recipientRepo.save(person);
//        }
        return rezult;
    }

    public List<AloneNewDto> getCommonNewsToPerson(String user) {
        List<AloneNewDto> rezult = new ArrayList<>();
        StandardInfo standardInfo = onlineInfo.get(user);
//        int size = standardInfo.getCommonGlobalRead().size();
        for (int i = 0; i < commonNewsGlobal.size(); i++) {
            if (i == standardInfo.getCommonGlobalRead().size()) standardInfo.getCommonGlobalRead().add(0);
            if ((commonGlobalMask.get(i) + standardInfo.getCommonGlobalRead().get(i)) == 0)
                rezult.add(commonNewsGlobal.get(i));
        }
//        if (size != standardInfo.getCommonGlobalRead().size()) {
//            Recipient person = recipientRepo.findByExternId(user);
//            person.setCommonReading(standardInfo.getCommonGlobalRead().toString().replace(" ", ""));
//            recipientRepo.save(person);
//        }
        return rezult;
    }
}
