package ru.memman.service;

import ru.memman.dtos.AloneNewDto;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.TokenUser;
import ru.memman.entity.*;
import ru.memman.enums.*;
import ru.memman.holders.StandardInfoHolder;
import ru.memman.mappers.AloneNewMapper;
import ru.memman.mappers.LetterLocalMapper;
import ru.memman.models.StandardInfo;
import ru.memman.repository.AloneNewRepo;
import ru.memman.repository.AloneNewRepository;
import ru.memman.repository.NotificationRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class MessageService implements SimpleStringService {
    private final StandardInfoHolder standardInfoHolder;
    private final AloneNewMapper aloneNewMapper;
    private final AloneNewRepo aloneNewRepo;
    private final RecipientService recipientService;
    private final NotificationRepo notificationRepo;
    private final TokenService tokenService;
    private final LinkedList<OnlineUserDto> inline;
    private final LocalisationService localisationService;
    private final LetterLocalMapper letterLocalMapper;
    private final AloneNewRepository aloneNewRepository;
    private final Map<String, Set<String>> rightsMap;

    @Transactional
    public String sendMessage(AloneNewDto aloneNewDto, boolean fromController) {
        TokenUser tokenUser = tokenService.getTokenUser();
        aloneNewDto.setCreationDate(new Timestamp(System.currentTimeMillis()));
        aloneNewDto.setSendingFrom((String) tokenUser.getClaims().get("sub"));
        aloneNewDto.setAlreadyRead(false);
        Recipient from = recipientService.getRecipient(tokenUser);
        if (aloneNewDto.getLocalisation() == null) aloneNewDto.setLocalisation(from.getLocalisation());
        Set<Recipient> to = new HashSet<>();
        if (fromController && tokenService.getPriorityRole(tokenUser).ordinal() > UserRole.MANAGER.ordinal()) {
            if (aloneNewDto.getAddressList() != null && !aloneNewDto.getAddressList().isEmpty()) {
                if (aloneNewDto.getAddressList().stream().anyMatch(x -> Objects.equals(x, standardInfoHolder.getSystemRecipientHard().getExternUuid())
                        || Objects.equals(x, standardInfoHolder.getInformerRecipientHard().getExternUuid())
                        || Objects.equals(x, standardInfoHolder.getCommonRecipientHard().getExternUuid())))
                    throw new RuntimeException("нельзя смердам писать основным рецепиентам");
                to.addAll(recipientService.getRecipientsByUuids(aloneNewDto.getAddressList()));
                if (to.isEmpty()) throw new RuntimeException("Ни одного адресата не найдено");
            } else throw new RuntimeException("адресаты не указаны");
        } else to.addAll(recipientService.getRecipientsByUuids(aloneNewDto.getAddressList()));
        AloneNew aloneNew = aloneNewMapper.dtoToEntity(aloneNewDto);
        if (aloneNew.getId() != null) aloneNew.setId(null);
        aloneNew.setSendFrom(from);
        aloneNew.setExistPrimePhoto(from.isExistPrimePhoto());
        switch (aloneNew.getCategory()) {
            case SYSTEM -> {
                Recipient sysRecipient = recipientService.findRecipient(1L).orElseThrow(() -> new RuntimeException("SystemRecipient not found"));
                if (to.isEmpty()) {
                    aloneNew.setSendTo(Collections.singleton(sysRecipient));
                    aloneNew.setAttention(Attention.SYSTEM);
                    sysRecipient.setSystemReading(sysRecipient.getSystemReading().concat(String.valueOf(0)));
                } else {
                    aloneNew.setSendFrom(sysRecipient);
                    aloneNewDto.setSendingFrom(standardInfoHolder.getSystemRecipientHard().getExternUuid());
                    aloneNew.setSendTo(to);
                    aloneNew.setNotReadingString(getInfoStringFromStringUuids(to.stream().map(Recipient::getExternUuid).collect(Collectors.toSet())));
                }
                aloneNew.setExistPrimePhoto(false);
            }
            case COMMON -> {
                Recipient commonRecipient = recipientService.findRecipient(2L).orElseThrow(() -> new RuntimeException("CommonRecipient not found"));
                aloneNew.setSendTo(Collections.singleton(commonRecipient));
                aloneNew.setAttention(Attention.COMMON);
                commonRecipient.setCommonReading(commonRecipient.getCommonReading().concat(String.valueOf(0)));
                aloneNew.setExistPrimePhoto(false);
            }
            default -> {
                if (to.isEmpty()) throw new RuntimeException("Адресат не указан");
                aloneNew.setSendTo(to);
                aloneNew.setExternId(from.getLinkExternId());
                aloneNew.setNotReadingString(getInfoStringFromStringUuids(to.stream().map(Recipient::getExternUuid).collect(Collectors.toSet())));
            }
        }
        if (aloneNewDto.getLetterLocals() != null && !aloneNewDto.getLetterLocals().isEmpty()) {
            aloneNew.setExistLocalisations(true);
            Collection<LetterLocal> letterLocalList = letterLocalMapper.collectionDtoToCollectionEntity(aloneNewDto.getLetterLocals());
            aloneNew.setLetterLocalisations(new ArrayList<>(letterLocalList));
            letterLocalList.forEach(x -> x.setMainLetter(aloneNew));
        }
        if (aloneNewDto.isExistPrimePhoto()) {
            aloneNew.setImageUrl(aloneNew.getCreationDate().toString());
            aloneNew.setExternId(from.getLinkExternId());
        }
        aloneNewRepository.saveNewLetter(aloneNew);
        log.info("New letter is saved. ID: {}", aloneNew.getId());
        aloneNewDto.setId(aloneNew.getId());
        aloneNewDto.setExistPrimePhoto(aloneNew.isExistPrimePhoto());
        aloneNewDto.setSendingFromAlt(from.getNickName());
        aloneNewDto.setImageUrl(aloneNew.getImageUrl());
        switch (aloneNew.getCategory()) {
            case SYSTEM -> {
                if (to.isEmpty()) {
                    aloneNewDto.setSendingFrom(String.valueOf(standardInfoHolder.getSystemNewsGlobal().size()));
                    aloneNewDto.setAttention(Attention.SYSTEM);
                    standardInfoHolder.getSystemNewsGlobal().add(aloneNewDto);
                    standardInfoHolder.getSystemGlobalMask().add(0);
                    inline.add(OnlineUserDto.builder().externUuid("system").build());
                    log.info("letter ${} is entered to holder", aloneNewDto);
                } else {
                    Set<Recipient> onlineRecipients = to.stream().filter(x -> standardInfoHolder.getOnlineInfo().containsKey(x.getExternUuid())).collect(Collectors.toSet());
                    for (Recipient recipient :
                            onlineRecipients) {
                        aloneNewDto.setHidden(false);
                        addLetterToHolderRecipient(localisationService.getLetterDtoTo(aloneNew, recipient, standardInfoHolder.getSystemRecipientHard(), true).orElseThrow(), recipient.getExternUuid(), false);
                    }
                }
            }
            case COMMON -> {
                aloneNewDto.setSendingFrom(String.valueOf(standardInfoHolder.getCommonNewsGlobal().size()));
                aloneNewDto.setAttention(Attention.COMMON);
                standardInfoHolder.getCommonNewsGlobal().add(aloneNewDto);
                standardInfoHolder.getCommonGlobalMask().add(0);
                inline.add(OnlineUserDto.builder().externUuid("common").build());
                log.info("letter ${} is entered to holder", aloneNewDto);
            }
            default -> {
                Set<Recipient> onlineRecipients = to.stream().filter(x -> standardInfoHolder.getOnlineInfo().containsKey(x.getExternUuid())).collect(Collectors.toSet());
                for (Recipient recipient :
                        onlineRecipients) {
                    aloneNewDto.setHidden(from.getSecretLevelPhoto() != SecretLevel.OPEN);
                    addLetterToHolderRecipient(localisationService.getLetterDtoTo(aloneNew, recipient, from, true).orElseThrow(), recipient.getExternUuid(), from.isExistPrimePhoto() && !aloneNewDto.isHidden());
                }
            }
        }
        return aloneNew.getImageUrl();
    }

    private void addLetterToHolderRecipient(AloneNewDto aloneNewDto, String externUuid, boolean rightPrime) {
        standardInfoHolder.addLetterToHolderRecipient(aloneNewDto, externUuid, rightPrime);
        log.info("letter ${} is entered to holder", aloneNewDto);
        sendChangeLetterCounts(externUuid);
    }

    public void sendChangeLetterCounts(String externUuid) {
        StandardInfo standardInfo = standardInfoHolder.getOnlineInfo().get(externUuid);

        int com = standardInfo.viewNewGlobalMessage(standardInfoHolder.getCommonGlobalMask(), standardInfo.getCommonGlobalRead());
        int sys = standardInfo.viewNewGlobalMessage(standardInfoHolder.getSystemGlobalMask(), standardInfo.getSystemGlobalRead());
        inline.add(OnlineUserDto.builder().externUuid(externUuid).newsCounts(new int[]{
                standardInfo.getCounts()[0] + com + sys,
                standardInfo.getCounts()[1] + sys,
                com,
                standardInfo.getCounts()[3],
                standardInfo.getCounts()[4]}).online(true).build());
    }

    public List<AloneNewDto> getCommonMessages(String user, boolean all, Localisation localisation) {
        return standardInfoHolder.getCommonNewsOfPerson(user, all, localisation);
    }

    public List<AloneNewDto> getSystemMessages(String user, boolean all, Localisation localisation) {
        List<AloneNewDto> global = standardInfoHolder.getGlobalSystemNewsOfPerson(user, all, localisation);
        global.addAll(standardInfoHolder.getOnlineInfo().get(user).getSystemNews());
        return global.stream().sorted(Comparator.comparing(AloneNewDto::getCreationDate)).toList();
    }


    @Transactional
    public void readOrRemoveGlobalMessage(String externUuid, Attention category, String id, boolean rr) {
        Recipient recipient = recipientService.findRecipient(externUuid);
        if (category == Attention.SYSTEM) {
            int element = Integer.parseInt(id);
            List<Integer> reading = standardInfoHolder.getOnlineInfo().get(externUuid).getSystemGlobalRead();
            if (rr) reading.set(element, 1);
            else reading.set(element, 3);
            recipient.setSystemReading(StringUtils.join(reading, ""));
            sendChangeLetterCounts(externUuid);
        } else if (category == Attention.COMMON) {
            int element = Integer.parseInt(id);
            List<Integer> reading = standardInfoHolder.getOnlineInfo().get(externUuid).getCommonGlobalRead();
            if (rr) reading.set(element, 1);
            else reading.set(element, 3);
            recipient.setCommonReading(StringUtils.join(reading, ""));
            sendChangeLetterCounts(externUuid);
        } else {
            readOrRemoveIndividualMessage(externUuid, UUID.fromString(id), rr);
        }

        recipientService.saveRecipient(recipient);
    }

    @Transactional
    public void readOrRemoveIndividualMessage(String externUuid, UUID id, boolean rr) {
        AloneNew letter = aloneNewRepo.findById(id).orElseThrow(() -> new RuntimeException("Letter not found"));
        if (standardInfoHolder.getOnlineInfo().get(externUuid) != null)
            standardInfoHolder.getOnlineInfo().get(externUuid).removeNewMessage(aloneNewMapper.entityToDto(letter));
        sendChangeLetterCounts(externUuid);
        if (rr) {
            removeUuidFromInfo(letter.getNotReadingString(), externUuid);
            aloneNewRepo.save(letter);
        } else {
            Set<Recipient> recipientList = letter.getSendTo();
            if (recipientList.size() == 1) aloneNewRepo.deleteById(id);
            else {
                Optional<Recipient> recipient = recipientList.stream().filter(x -> Objects.equals(x.getExternUuid(), externUuid)).findFirst();
                recipient.ifPresent(recipientList::remove);
                aloneNewRepo.save(letter);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<AloneNewDto> getAllNewsByCategory(String user, NewsCategory category, String loc) {
        Recipient recipient = notificationRepo.findRecipientWithReceivedLetter(user);
        if (recipient != null && !recipient.getReceivedLetters().isEmpty()) {
            List<AloneNew> aloneNewList = notificationRepo.getLettersWithSendersByCategory(recipient.getReceivedLetters(), category);
            if (aloneNewList.isEmpty()) return new ArrayList<>();
            List<AloneNewDto> resultList = new ArrayList<>();
            recipientService.detachRecipient(recipient);
            recipient.setLocalisation(tokenService.getLocalisation(loc));
            for (AloneNew letter :
                    aloneNewList) {
                Optional<AloneNewDto> letterDto = localisationService.getLetterDtoTo(letter, recipient, letter.getSendFrom(), false);
                letterDto.ifPresent(x -> {
                    if (letter.getSendFrom() != null && x.getSendingFrom() != null && !x.getSendingFrom().equals(standardInfoHolder.getInformerRecipientHard().getExternUuid())) {
                        x.setHidden(letter.getSendFrom().getSecretLevelPhoto() != SecretLevel.OPEN);
                        standardInfoHolder.rightsConfirm(x, recipient.getExternUuid(), letter.getSendFrom() != null && letter.getSendFrom().isExistPrimePhoto() && !x.isHidden());
                    }
                    resultList.add(x);
                });
            }
            return resultList;
        } else return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<AloneNewDto> getFreshNewsByCategory(NewsCategory category, String token, String loc) {
        List<AloneNewDto> aloneNewDtoList = switch (category) {
            case FAMILY -> standardInfoHolder.getOnlineInfo().get(token).getFamilyNews();
            case PRIVATE -> standardInfoHolder.getOnlineInfo().get(token).getIndividualNews();
            default -> new ArrayList<>();
        };
        Localisation localisation = tokenService.getLocalisation(loc);
        if (!aloneNewDtoList.isEmpty() && aloneNewDtoList.get(0).getLocalisation() != localisation) {
            List<AloneNewDto> covertDtoList = new ArrayList<>();
            for (AloneNewDto dto :
                    aloneNewDtoList) {
                covertDtoList.add(localisationService.getLetterOnLocalisation(dto, localisation));
            }
            return covertDtoList;
        }
        return aloneNewDtoList;
    }

    @Transactional
    public String acceptForMessage(String user, UUID uuid) {


        return "Accept is counting";
    }

    @Transactional
    public String rejectForMessage(String user, UUID uuid) {
        return "Reject is counting";
    }

    public boolean checkRights(String usedUuid, String senderUuid) {
        log.info("request for {} file: {}", usedUuid, senderUuid);
        return rightsMap.containsKey(usedUuid) && rightsMap.get(usedUuid).contains(senderUuid);
    }
}
