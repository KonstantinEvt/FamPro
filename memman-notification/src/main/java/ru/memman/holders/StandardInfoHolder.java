package ru.memman.holders;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import ru.memman.dtos.AloneNewDto;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.entity.AloneNew;
import ru.memman.entity.Recipient;
import ru.memman.enums.Attention;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.Localisation;
import ru.memman.enums.SwitchPosition;
import ru.memman.mappers.AloneNewMapper;
import ru.memman.mappers.LetterLocalMapper;
import ru.memman.models.StandardInfo;
import ru.memman.repository.AloneNewRepository;
import ru.memman.repository.RecipientRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.service.LocalisationService;

import java.util.*;

@Component
@Log4j2
@Getter
@Setter
public class StandardInfoHolder {
    private final AloneNewMapper aloneNewMapper;
    private final LetterLocalMapper letterLocalMapper;
    private final AloneNewRepository aloneNewRepository;
    private final RecipientRepository recipientRepository;

    private final Map<String, StandardInfo> onlineInfo;
    private final List<AloneNewDto> systemNewsGlobal;
    private final List<AloneNewDto> commonNewsGlobal;
    private final List<Integer> systemGlobalMask;
    private final List<Integer> commonGlobalMask;
    private final LocalisationService localisationService;
    private final Map<String, Set<String>> rightsMap;
    private final LinkedList<List<DirectiveGuards>> directiveRights;
    private final LinkedList<OnlineUserDto> inline;
    @Value("${spring.application.names.system}")
    private String systemName;
    @Value("${spring.application.names.common}")
    private String commonName;
    @Value("${spring.application.names.informer}")
    private String informerName;

    private final Recipient systemRecipientHard;
    private final Recipient commonRecipientHard;
    private final Recipient informerRecipientHard;

    public StandardInfoHolder(
            AloneNewMapper aloneNewMapper,
            LetterLocalMapper letterLocalMapper,
            AloneNewRepository aloneNewRepository,
            RecipientRepository recipientRepository,
            Map<String, StandardInfo> onlineInfo,
            List<AloneNewDto> systemNewsGlobal,
            List<AloneNewDto> commonNewsGlobal,
            List<Integer> systemGlobalMask,
            List<Integer> commonGlobalMask,
            LocalisationService localisationService,
            Map<String, Set<String>> rightsMap,
            LinkedList<List<DirectiveGuards>> directiveRights,
            LinkedList<OnlineUserDto> inline,
            @Qualifier("systemRecipientHard") Recipient systemRecipientHard,
            @Qualifier("commonRecipientHard") Recipient commonRecipientHard,
            @Qualifier("informerRecipientHard") Recipient informerRecipientHard
    ) {
        this.aloneNewMapper = aloneNewMapper;
        this.letterLocalMapper = letterLocalMapper;
        this.aloneNewRepository = aloneNewRepository;
        this.recipientRepository = recipientRepository;
        this.onlineInfo = onlineInfo;
        this.systemNewsGlobal = systemNewsGlobal;
        this.commonNewsGlobal = commonNewsGlobal;
        this.systemGlobalMask = systemGlobalMask;
        this.commonGlobalMask = commonGlobalMask;
        this.localisationService = localisationService;
        this.rightsMap = rightsMap;
        this.directiveRights = directiveRights;
        this.inline = inline;
        this.systemRecipientHard = systemRecipientHard;
        this.commonRecipientHard = commonRecipientHard;
        this.informerRecipientHard = informerRecipientHard;
    }

    @PostConstruct
    @Transactional
    public void setUpMainRecipients() {
        systemRecipientHard.setExternUuid(UUID.nameUUIDFromBytes(this.systemName.getBytes()).toString());
        commonRecipientHard.setExternUuid(UUID.nameUUIDFromBytes(this.commonName.getBytes()).toString());
        informerRecipientHard.setExternUuid(UUID.nameUUIDFromBytes(this.informerName.getBytes()).toString());
        Optional<Recipient> systemRecipient = recipientRepository.getRecipientWithReceiveLettersById(1L);
        Optional<Recipient> commonRecipient = recipientRepository.getRecipientWithReceiveLettersById(2L);
        if (systemRecipient.isPresent() && commonRecipient.isPresent()) {
            List<AloneNew> sysLetters = systemRecipient.get().getReceivedLetters();
            List<AloneNew> letterWithLocalisations;
            if (sysLetters != null && !sysLetters.isEmpty()) {
                letterWithLocalisations = aloneNewRepository.getLettersWithLetterLocals(sysLetters.stream().filter(AloneNew::isExistLocalisations).toList());
                systemNewsGlobal.addAll(sysLetters
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(x -> Objects.equals(x.getAttention(), Attention.SYSTEM))
                        .sorted(Comparator.comparing(AloneNew::getCreationDate))
                        .map(aloneNewMapper::entityToDto)
                        .peek(System.out::println)
                        .toList());

            } else letterWithLocalisations = new ArrayList<>();
            for (int i = 0; i < systemNewsGlobal.size(); i++) {
                AloneNewDto sysAloneDto = systemNewsGlobal.get(i);
                sysAloneDto.setSendingFrom(String.valueOf(i));
                if (sysAloneDto.isExistLocalisations()) {
                    Optional<AloneNew> sysAlone = letterWithLocalisations.stream().filter(x -> Objects.equals(x.getExternId(), sysAloneDto.getExternId())).findFirst();
                    sysAlone.ifPresent(aloneNew -> sysAloneDto.setLetterLocals(new ArrayList<>(letterLocalMapper.collectionEntityToCollectionDto(aloneNew.getLetterLocalisations()))));
                }
            }
            systemGlobalMask.addAll(systemRecipient.get().getSystemReading().chars().map(x -> x - 48).boxed().toList());
            List<AloneNew> comLetters = commonRecipient.get().getReceivedLetters();
            if (comLetters != null && !comLetters.isEmpty()) {
                letterWithLocalisations = aloneNewRepository.getLettersWithLetterLocals(comLetters.stream().filter(AloneNew::isExistLocalisations).toList());
                commonNewsGlobal.addAll(comLetters
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(x -> Objects.equals(x.getAttention(), Attention.COMMON))
                        .sorted(Comparator.comparing(AloneNew::getCreationDate))
                        .map(aloneNewMapper::entityToDto)
                        .toList());
            } else letterWithLocalisations = new ArrayList<>();
            for (int i = 0; i < commonNewsGlobal.size(); i++) {
                AloneNewDto comAloneDto = commonNewsGlobal.get(i);
                commonNewsGlobal.get(i).setSendingFrom(String.valueOf(i));
                if (comAloneDto.isExistLocalisations()) {
                    Optional<AloneNew> sysAlone = letterWithLocalisations.stream().filter(x -> Objects.equals(x.getExternId(), comAloneDto.getExternId())).findFirst();
                    sysAlone.ifPresent(aloneNew -> comAloneDto.setLetterLocals(new ArrayList<>(letterLocalMapper.collectionEntityToCollectionDto(aloneNew.getLetterLocalisations()))));
                }
            }
            commonGlobalMask.addAll(commonRecipient.get().getCommonReading().chars().map(x -> x - 48).boxed().toList());
        } else {
            recipientRepository.persistNewRecipient(systemRecipientHard);
            recipientRepository.persistNewRecipient(commonRecipientHard);
            recipientRepository.persistNewRecipient(informerRecipientHard);
        }

    }

    public void addNewMessageToPerson(AloneNewDto aloneNewDto, String externUuid) {
        if (onlineInfo.containsKey(externUuid)) {
            onlineInfo.get(externUuid).addNewMessage(aloneNewDto);
            sendChangeLetterCounts(externUuid);
        }
    }

    public void removeMessageFromPerson(String externUuid, AloneNew aloneNew) {
        System.out.println("delete from holder");
        if (onlineInfo.containsKey(externUuid)) {
            onlineInfo.get(externUuid).removeNewMessage(aloneNewMapper.entityToDto(aloneNew));
            sendChangeLetterCounts(externUuid);
        }
    }

    public List<AloneNewDto> getGlobalSystemNewsOfPerson(String user, boolean all, Localisation localisation) {
        List<AloneNewDto> result = new ArrayList<>();
        StandardInfo standardInfo = onlineInfo.get(user);
        for (int i = 0; i < systemNewsGlobal.size(); i++) {
            if (i == standardInfo.getSystemGlobalRead().size()) standardInfo.getSystemGlobalRead().add(0);
            int localMask = systemGlobalMask.get(i) + standardInfo.getSystemGlobalRead().get(i);
            if ((!all && localMask == 0) || (all && localMask < 3)) {
                AloneNewDto letter = localisationService.getLetterOnLocalisation(systemNewsGlobal.get(i), localisation);
                if (letter.getCreationDate().before(standardInfo.getCreated())) letter.setCreationDate(standardInfo.getCreated());
                if (all && localMask != 0)
                    letter.setAlreadyRead(true);
                result.add(letter);
            }
        }
        return result;
    }

    public List<AloneNewDto> getCommonNewsOfPerson(String user, boolean all, Localisation localisation) {
        List<AloneNewDto> result = new ArrayList<>();
        StandardInfo standardInfo = onlineInfo.get(user);
        for (int i = 0; i < commonNewsGlobal.size(); i++) {
            if (i == standardInfo.getCommonGlobalRead().size()) standardInfo.getCommonGlobalRead().add(0);
            int localMask = commonGlobalMask.get(i) + standardInfo.getCommonGlobalRead().get(i);
            if ((!all && localMask == 0) || (all && localMask < 3)) {
                AloneNewDto letter = localisationService.getLetterOnLocalisation(commonNewsGlobal.get(i), localisation);
                if (letter.getCreationDate().before(standardInfo.getCreated())) letter.setCreationDate(standardInfo.getCreated());
                if (all && localMask != 0)
                    letter.setAlreadyRead(true);
                result.add(letter);
            }
        }
        return result;
    }

    public void addLetterPackToHolderRecipient(List<AloneNewDto> aloneNewDto, String externUuid) {
        log.info("letters added to holder");
        if (onlineInfo.containsKey(externUuid)) {
            StandardInfo standardInfo=onlineInfo.get(externUuid);
            aloneNewDto.forEach(standardInfo::addNewMessage);
        }
    }

    public void addLetterToHolderRecipient(AloneNewDto aloneNewDto, String externUuid, boolean rightPrime) {
        log.info("letter {} is entered to holder", aloneNewDto.getSubject());
        if (onlineInfo.containsKey(externUuid)) {
            DirectiveGuards dr=rightsConfirm(aloneNewDto, externUuid, rightPrime);
            if (dr!=null) directiveRights.add(List.of(dr));
            onlineInfo.get(externUuid).addNewMessage(aloneNewDto);
            sendChangeLetterCounts(externUuid);
        }
    }

    public DirectiveGuards rightsConfirm(AloneNewDto aloneNewDto, String externUuid, boolean rightPrime) {
        if (aloneNewDto.getSendingFrom() != null && !Objects.equals(aloneNewDto.getSendingFrom(), informerRecipientHard.getExternUuid()) && aloneNewDto.getAttention() != Attention.COMMON) {
            Set<String> rightAgree = new HashSet<>();
            rightsMap.putIfAbsent(externUuid, new HashSet<>());
            if (aloneNewDto.getAttention() != Attention.SYSTEM && aloneNewDto.isExistPrimePhoto() && rightPrime) {
                String accessLittle = String.valueOf(SwitchPosition.LITTLE.ordinal()).concat(aloneNewDto.getExternId());
                rightsMap.get(externUuid).add(accessLittle);
                rightAgree.add(accessLittle);
            }
            if (aloneNewDto.getImageUrl() != null && !aloneNewDto.getImageUrl().isBlank() && (aloneNewDto.getAttention() != Attention.SYSTEM || Objects.equals(aloneNewDto.getSendingFrom(), systemRecipientHard.getExternUuid()))) {
                String accessMain = String.valueOf(SwitchPosition.MAIN.ordinal()).concat(aloneNewDto.getImageUrl());
                rightsMap.get(externUuid).add(accessMain);
                rightAgree.add(accessMain);
                log.info(accessMain);
            }
//            directiveRights.add(DirectiveGuards.builder().tokenUser(externUuid).guards(rightAgree).operation(KafkaOperation.ADD).build());
        return DirectiveGuards.builder().tokenUser(externUuid).guards(rightAgree).operation(KafkaOperation.ADD).build();
        }
        return null;
    }

    public void sendChangeLetterCounts(String externUuid) {
        StandardInfo standardInfo = getOnlineInfo().get(externUuid);
        int com = standardInfo.viewNewGlobalMessage(getCommonGlobalMask(), standardInfo.getCommonGlobalRead());
        int sys = standardInfo.viewNewGlobalMessage(getSystemGlobalMask(), standardInfo.getSystemGlobalRead());
        inline.add(OnlineUserDto.builder().externUuid(externUuid).newsCounts(new int[]{
                standardInfo.getCounts()[0] + com + sys,
                standardInfo.getCounts()[1] + sys,
                com,
                standardInfo.getCounts()[3],
                standardInfo.getCounts()[4]}).online(true).build());
    }
}
