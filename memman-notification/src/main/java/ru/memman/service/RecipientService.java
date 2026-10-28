package ru.memman.service;

import jakarta.annotation.PostConstruct;
import ru.memman.dtos.*;
import ru.memman.entity.AloneNew;
import ru.memman.entity.Recipient;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.SecretLevel;
import ru.memman.holders.StandardInfoHolder;
import ru.memman.mappers.AloneNewMapper;
import ru.memman.models.StandardInfo;
import ru.memman.repository.NotificationRepo;
import ru.memman.repository.RecipientRepo;
import ru.memman.repository.RecipientRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@Log4j2
public class RecipientService implements SimpleStringService {
    private final RecipientRepo recipientRepo;
    private final StandardInfoHolder standardInfoHolder;
    private final AloneNewMapper letterMapper;
    private final NotificationRepo notificationRepo;
    private final LinkedList<DirectiveGuards> directiveRights;
    private final RecipientRepository recipientRepository;
    private final LinkedList<OnlineUserDto> inline;
    private final LocalisationService localisationService;

    public RecipientService(RecipientRepo recipientRepo,
                            StandardInfoHolder standardInfoHolder,
                            AloneNewMapper letterMapper,
                            NotificationRepo notificationRepo,
                            LinkedList<DirectiveGuards> directiveRights,
                            RecipientRepository recipientRepository,
                            LinkedList<OnlineUserDto> inline,
                            LocalisationService localisationService) {
        this.recipientRepo = recipientRepo;
        this.standardInfoHolder = standardInfoHolder;
        this.letterMapper = letterMapper;
        this.notificationRepo = notificationRepo;
        this.directiveRights = directiveRights;
        this.recipientRepository = recipientRepository;
        this.inline = inline;
        this.localisationService = localisationService;
    }

    @PostConstruct
    public void reloadInline() {
        inline.add(new OnlineUserDto());
    }

    @Transactional(readOnly = true)
    public Recipient findRecipient(String externId) {
        return recipientRepo.findByExternUuid(externId).orElse(null);
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
    public void mergeRecipient(OnlineUserDto onlineUserDto) {
        onlineUserDto.setSendOver(false);
        if (onlineUserDto.getExternUuid() == null) inline.add(onlineUserDto);
        else {
            Optional<Recipient> recipient = recipientRepository.getRecipientByExternId(onlineUserDto.getExternUuid());
            log.info("User inline: {}", onlineUserDto);
            StandardInfo standardInfo;
            if (recipient.isEmpty()) {
                standardInfo = new StandardInfo();
                standardInfo.setLocalisation(onlineUserDto.getLocalisation());
                Recipient inlineRecipient = Recipient.builder()
                        .email(onlineUserDto.getEmail())
                        .externUuid(onlineUserDto.getExternUuid())
                        .linkExternId(onlineUserDto.getLinkExternId())
                        .nickName(onlineUserDto.getNickName())
                        .commonReading("")
                        .systemReading("")
                        .localisation(onlineUserDto.getLocalisation())
                        .existPrimePhoto(onlineUserDto.isPrimePhoto())
                        .secretLevelPhoto(onlineUserDto.getSecretLevelPhoto())
                        .lastUpdate(onlineUserDto.getLastUpdate())
                        .build();
                recipientRepository.persistNewRecipient(inlineRecipient);
                standardInfoHolder.getOnlineInfo().put(onlineUserDto.getExternUuid(), standardInfo);
            } else {
                boolean change = false;
                boolean onlineChange = false;
                standardInfo = standardInfoHolder.getOnlineInfo().get(onlineUserDto.getExternUuid());

                if (onlineUserDto.getLocalisation() != null && !Objects.equals(recipient.get().getLocalisation(), onlineUserDto.getLocalisation())) {
                    recipient.get().setLocalisation(onlineUserDto.getLocalisation());
                    change = true;
                    onlineChange = true;
                }
                if (onlineUserDto.getEmail() != null && !Objects.equals(recipient.get().getEmail(), onlineUserDto.getEmail())) {
                    recipient.get().setEmail(onlineUserDto.getEmail());
                    change = true;
                }
                if (onlineUserDto.getLinkExternId() != null && !Objects.equals(recipient.get().getLinkExternId(), onlineUserDto.getLinkExternId())) {
                    recipient.get().setLinkExternId(onlineUserDto.getLinkExternId());
                    change = true;
                }
                if (onlineUserDto.isPrimePhoto() && !recipient.get().isExistPrimePhoto()) {
                    recipient.get().setExistPrimePhoto(true);
                    change = true;
                }
                if (onlineUserDto.getNickName() != null && !Objects.equals(recipient.get().getNickName(), onlineUserDto.getNickName())) {
                    recipient.get().setNickName(onlineUserDto.getNickName());
                    change = true;
                }
                if (onlineUserDto.getLastUpdate() != null && !Objects.equals(recipient.get().getLastUpdate(), onlineUserDto.getLastUpdate())) {
                    recipient.get().setLastUpdate(onlineUserDto.getLastUpdate());
                    change = true;
                }
                if (onlineUserDto.getSecretLevelPhoto() != null && !Objects.equals(recipient.get().getSecretLevelPhoto(), onlineUserDto.getSecretLevelPhoto())) {
                    recipient.get().setSecretLevelPhoto(onlineUserDto.getSecretLevelPhoto());
                    change = true;
                }
                if (!onlineUserDto.isOnline()) {
                    recipient.get().setLastOnline(onlineUserDto.getLastOnline());
                    standardInfoHolder.getOnlineInfo().remove(onlineUserDto.getExternUuid());
                    change = true;
                }
                if (change) recipientRepository.update(recipient.get());
                if ((standardInfo == null || onlineChange) && onlineUserDto.isOnline()) {
                    StandardInfo reloadInfo = new StandardInfo();
                    reloadInfo.setLocalisation(onlineUserDto.getLocalisation());
                    reloadOnlineRecipient(recipient.get(), reloadInfo);
                    standardInfo = reloadInfo;
                }
            }
            if (onlineUserDto.isOnline()) {
                int com = standardInfo.viewNewGlobalMessage(standardInfoHolder.getCommonGlobalMask(), standardInfo.getCommonGlobalRead());
                int sys = standardInfo.viewNewGlobalMessage(standardInfoHolder.getSystemGlobalMask(), standardInfo.getSystemGlobalRead());
                onlineUserDto.setNewsCounts(new int[]{
                        standardInfo.getCounts()[0] + com + sys,
                        standardInfo.getCounts()[1] + sys,
                        com,
                        standardInfo.getCounts()[3],
                        standardInfo.getCounts()[4]
                });
                inline.add(onlineUserDto);
            } else recipient.ifPresent(x -> directiveRights.add(DirectiveGuards.builder().operation(KafkaOperation.EXIT)
                    .tokenUser(x.getExternUuid())
                    .build()));
        }
    }

    @Transactional
    public Set<Recipient> getRecipientsByUuids(Set<String> externUuids) {
        return recipientRepository.getRecipients(externUuids);
    }

    @Transactional
    public void detachRecipient(Recipient recipient) {
        recipientRepository.detach(recipient);
    }

//    @Transactional
//    public void changeRecipient(DirectiveGuards directiveGuards) {
//        Recipient requester = findRecipient(directiveGuards.getTokenUser());
//        requester.setExistPrimePhoto(directiveGuards.isPhotoExist());
//        requester.setLinkExternId(directiveGuards.getPerson());
//        recipientRepository.update(requester);
//    }

    @Transactional
    public Recipient creatRecipient(TokenUser tokenUser) {
        Recipient recipient = Recipient.builder()
                .nickName(tokenUser.getNickName())
                .externUuid((String) tokenUser.getClaims().get("sub"))
                .commonReading("")
                .systemReading("")
                .build();
        if (tokenUser.getEmail() != null) recipient.setEmail(tokenUser.getEmail());
        recipientRepo.save(recipient);
        return recipient;
    }

    @Transactional
    public void saveRecipient(Recipient recipient) {
        recipientRepo.save(recipient);
    }

    @Transactional
    public void reloadOnlineRecipient(Recipient recipient, StandardInfo standardInfo) {
        standardInfoHolder.getOnlineInfo().put(recipient.getExternUuid(), standardInfo);
        List<AloneNew> receivedLetters = recipient.getReceivedLetters();
        if (receivedLetters != null && !receivedLetters.isEmpty()) {
            List<AloneNew> listWithSenders = notificationRepo.getFullLettersWithSenders(receivedLetters);
            for (AloneNew letter :
                    listWithSenders) {
                Recipient from = letter.getSendFrom();
                Optional<AloneNewDto> letterDto = localisationService.getLetterDtoTo(letter, recipient, from, true);
                letterDto.ifPresent(x -> {
                    if (from == null) x.setHidden(false);
                    else x.setHidden(from.getSecretLevelPhoto() != SecretLevel.OPEN);
                    standardInfoHolder.addLetterToHolderRecipient(x, recipient.getExternUuid(), from != null && from.isExistPrimePhoto() && !x.isHidden());
                });
            }
        }
        standardInfo.getSystemGlobalRead().addAll(recipient.getSystemReading().chars().map(x -> x - 48).boxed().toList());
        standardInfo.getCommonGlobalRead().addAll(recipient.getCommonReading().chars().map(x -> x - 48).boxed().toList());
    }
}

