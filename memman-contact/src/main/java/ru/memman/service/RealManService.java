package ru.memman.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.dtos.ContactDto;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.RecipientDto;
import ru.memman.entity.RealMen;
import ru.memman.holders.OnlineContactHolder;
import ru.memman.repository.RealManRepository;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

@Service
@Getter
@Setter
@Log4j2
public class RealManService {
    private final OnlineContactHolder onlineContactHolder;
    private final ContactService contactService;
    private final RealManRepository realManRepository;
    private final LinkedList<OnlineUserDto> inline;


    public RealManService(OnlineContactHolder onlineContactHolder,
                          ContactService contactService,
                          RealManRepository realManRepository,
                          LinkedList<OnlineUserDto> inline) {
        this.onlineContactHolder = onlineContactHolder;
        this.contactService = contactService;
        this.realManRepository = realManRepository;
        this.inline = inline;
    }

    @Transactional
    public void mergeRealMen(OnlineUserDto onlineUserDto) {
        inline.add(onlineUserDto);
        if (onlineUserDto.getExternUuid() != null) {
            boolean change = false;
            RealMen realMen = onlineContactHolder.getOnlineManMap().get(onlineUserDto.getExternUuid());
            if (realMen == null) {
                realMen = realManRepository.getRealManByExternId(onlineUserDto.getExternUuid()).orElse(null);
                if (realMen != null && onlineUserDto.isOnline()) {
                    log.info(realMen.getExternUuid());
                    onlineContactHolder.getOnlineManMap().put(onlineUserDto.getExternUuid(), realMen);
                    log.info("User inline1: {}", onlineUserDto);
                    contactService.addContactsToHolder(realMen);
                    log.info("User inline2: {}", onlineUserDto);
                }
            } else if (!realMen.isOnline() && onlineUserDto.isOnline()) {
                RealMen realMen1 = realManRepository.getRealManByExternId(onlineUserDto.getExternUuid()).orElse(null);
                if (realMen1 != null) {
                    if (realMen1.getLocalisation() != realMen.getLocalisation())
                        realMen1.setLocalisation(realMen.getLocalisation());
                    contactService.addContactsToHolder(realMen1);
                }
            }else if (realMen.isOnline() && !onlineUserDto.isOnline()) onlineContactHolder.getOnlineContactsMap().remove(onlineUserDto.getExternUuid());
            if (realMen == null) {
                realMen = RealMen.builder()
                        .email(onlineUserDto.getEmail())
                        .externUuid(onlineUserDto.getExternUuid())
                        .linkExternId(onlineUserDto.getLinkExternId())
                        .nickName(onlineUserDto.getNickName())
                        .localisation(onlineUserDto.getLocalisation())
                        .secretLevelPhoto(onlineUserDto.getSecretLevelPhoto())
                        .secretLevelPhone(onlineUserDto.getSecretLevelPhone())
                        .secretLevelEmail(onlineUserDto.getSecretLevelEmail())
                        .mainPhone(onlineUserDto.getMainPhone())
                        .mainEmail(onlineUserDto.getMainEmail())
                        .existPrimePhoto(onlineUserDto.isPrimePhoto())
                        .lastUpdate(onlineUserDto.getLastUpdate())
                        .lastOnline(onlineUserDto.getLastOnline())
                        .lastTimeUse(new Timestamp(System.currentTimeMillis()))
                        .online(onlineUserDto.isOnline())
                        .build();
                realManRepository.persistNewRealMan(realMen);
                if (onlineUserDto.isOnline()) {
                    log.info("User first inline: {}", onlineUserDto);
                    onlineContactHolder.getOnlineManMap().put(onlineUserDto.getExternUuid(), realMen);
                    onlineContactHolder.getOnlineContactsMap().put(onlineUserDto.getExternUuid(), new HashSet<>());
                }
                realManRepository.detach(realMen);
            } else {
                if (onlineUserDto.getLocalisation() != null && !Objects.equals(realMen.getLocalisation(), onlineUserDto.getLocalisation())) {
                    realMen.setLocalisation(onlineUserDto.getLocalisation());
                    change = true;
                }
                if (onlineUserDto.getEmail() != null && !Objects.equals(realMen.getEmail(), onlineUserDto.getEmail())) {
                    realMen.setEmail(onlineUserDto.getEmail());
                    change = true;
                }
                if (onlineUserDto.getLinkExternId() != null && !Objects.equals(realMen.getLinkExternId(), onlineUserDto.getLinkExternId())) {
                    realMen.setLinkExternId(onlineUserDto.getLinkExternId());
                    change = true;
                }
                if (onlineUserDto.isPrimePhoto() != realMen.isExistPrimePhoto()) {
                    realMen.setExistPrimePhoto(onlineUserDto.isPrimePhoto());
                    change = true;
                }
                if (onlineUserDto.getSecretLevelPhoto() != null && onlineUserDto.getSecretLevelPhoto() != realMen.getSecretLevelPhoto()) {
                    realMen.setSecretLevelPhoto(onlineUserDto.getSecretLevelPhoto());
                    change = true;
                }
                if (onlineUserDto.getSecretLevelEmail() != null && onlineUserDto.getSecretLevelEmail() != realMen.getSecretLevelEmail()) {
                    realMen.setSecretLevelEmail(onlineUserDto.getSecretLevelEmail());
                    change = true;
                }
                if (onlineUserDto.getSecretLevelPhone() != null && onlineUserDto.getSecretLevelPhone() != realMen.getSecretLevelPhone()) {
                    realMen.setSecretLevelPhone(onlineUserDto.getSecretLevelPhone());
                    change = true;
                }
                if (onlineUserDto.getMainEmail() != null && !onlineUserDto.getMainEmail().equals(realMen.getMainEmail())) {
                    realMen.setMainEmail(onlineUserDto.getMainEmail());
                    change = true;
                }
                if (onlineUserDto.getMainPhone() != null && !onlineUserDto.getMainPhone().equals(realMen.getMainPhone())) {
                    realMen.setMainPhone(onlineUserDto.getMainPhone());
                    change = true;
                }
                if (onlineUserDto.getLastUpdate() != null && !Objects.equals(realMen.getLastUpdate(), onlineUserDto.getLastUpdate())) {
                    realMen.setLastUpdate(onlineUserDto.getLastUpdate());
                    change = true;
                }
                if (onlineUserDto.getNickName() != null && !Objects.equals(realMen.getNickName(), onlineUserDto.getNickName())) {
                    realMen.setNickName(onlineUserDto.getNickName());
                    change = true;
                }
                if (onlineUserDto.getLastOnline() != null && !Objects.equals(realMen.getLastOnline(), onlineUserDto.getLastOnline())) {
                    realMen.setLastOnline(onlineUserDto.getLastOnline());
                }
                realMen.setLastTimeUse(new Timestamp(System.currentTimeMillis()));
                realMen.setOnline(onlineUserDto.isOnline());
                if (change) {
                    RealMen realMenRenew = realManRepository.getRealManByExternId(onlineUserDto.getExternUuid()).orElseThrow(() -> new RuntimeException("User in base must be has"));
                    realMenRenew.setLastOnline(realMen.getLastOnline());
                    realMenRenew.setLastTimeUse(realMen.getLastTimeUse());
                    realMenRenew.setLastUpdate(realMen.getLastUpdate());
                    realMenRenew.setExistPrimePhoto(realMen.isExistPrimePhoto());
                    realMenRenew.setSecretLevelPhoto(realMen.getSecretLevelPhoto());
                    realMenRenew.setSecretLevelEmail(realMen.getSecretLevelEmail());
                    realMenRenew.setSecretLevelPhone(realMen.getSecretLevelPhone());
                    realMenRenew.setMainEmail(realMen.getMainEmail());
                    realMenRenew.setMainPhone(realMen.getMainPhone());
                    realMenRenew.setLocalisation(realMen.getLocalisation());
                    realMenRenew.setLinkExternId(realMen.getLinkExternId());
                    realMenRenew.setNickName(realMen.getNickName());
                    realMenRenew.setEmail(realMen.getEmail());
                    realManRepository.update(realMenRenew);

                }
            }
        }
    }

    public void addContactToOwner(String userUuid, String externId) {
    }

}

