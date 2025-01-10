package com.example.checks;

import com.example.dtos.*;
import com.example.transcriters.TranscriterHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component

public class CheckFamilyMember {
    CommonWordChecks commonWordChecks;
    Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$");
    Pattern phonePattern = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$");

    public CheckFamilyMember(CommonWordChecks commonWordChecks) {
        this.commonWordChecks = commonWordChecks;
    }

    public void check(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto) {
        checkFio(transcriterHolder, familyMemberDto);
        if (familyMemberDto.getMotherFio() != null)
            if (!checkFio(transcriterHolder, familyMemberDto.getMotherFio())) familyMemberDto.setMotherFio(null);
        if (familyMemberDto.getFatherFio() != null)
            if (!checkFio(transcriterHolder, familyMemberDto.getFatherFio())) familyMemberDto.setFatherFio(null);
        if (familyMemberDto.getDeathday() != null) if (familyMemberDto.getBirthday() != null
                && (familyMemberDto.getDeathday().before(familyMemberDto.getBirthday())))
            familyMemberDto.setDeathday(null);
        if (familyMemberDto.getMemberInfo() != null) {
            if (familyMemberDto.getMemberInfo().getMainEmail() != null)
                if (!checkEmail(familyMemberDto.getMemberInfo().getMainEmail())) familyMemberDto.getMemberInfo().setMainEmail(null);
            if (familyMemberDto.getMemberInfo().getEmails() != null) {
                Set<EmailDto> emails = new HashSet<>();
                for (EmailDto email : familyMemberDto.getMemberInfo().getEmails()) {
                    if (checkEmail(email.getInternName())) emails.add(email);
                }
                if (emails.isEmpty()) familyMemberDto.getMemberInfo().setEmails(null);
                else familyMemberDto.getMemberInfo().setEmails(emails);
            }
            if (familyMemberDto.getMemberInfo().getMainPhone() != null)
                if (!checkPhone(familyMemberDto.getMemberInfo().getMainPhone())) familyMemberDto.getMemberInfo().setMainPhone(null);
            if (familyMemberDto.getMemberInfo().getPhones() != null) {
                Set<PhoneDto> phones = new HashSet<>();
                for (PhoneDto phone : familyMemberDto.getMemberInfo().getPhones()) {
                    if (checkPhone(phone.getInternName())) phones.add(phone);
                }
                if (phones.isEmpty()) familyMemberDto.getMemberInfo().setPhones(null);
                else familyMemberDto.getMemberInfo().setPhones(phones);
            }
            if (familyMemberDto.getMemberInfo().getAddresses() != null) {
                Set<AddressDto> addressDtos = new HashSet<>();
                for (AddressDto address : familyMemberDto.getMemberInfo().getAddresses()) {
                    if (checkPlace(transcriterHolder, address)) {
                        checkAddressFM(transcriterHolder, address);
                        addressDtos.add(address);
                    }
                }
                if (addressDtos.isEmpty()) familyMemberDto.getMemberInfo().setAddresses(null);
                else familyMemberDto.getMemberInfo().setAddresses(addressDtos);
            }
        }
        if (familyMemberDto.getBurial() != null)
            if (!checkPlace(transcriterHolder, familyMemberDto.getBurial())) familyMemberDto.setBurial(null);
            else checkBurial(transcriterHolder,familyMemberDto.getBurial());
        if (familyMemberDto.getBirth() != null)
            if (!checkPlace(transcriterHolder, familyMemberDto.getBirth())) familyMemberDto.setBirth(null);
            else checkBirth(transcriterHolder, familyMemberDto.getBirth());

        if (familyMemberDto.getFioDtos() != null) {
            Set<FioDto> oldNames = new HashSet<>();
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                if (checkFio(transcriterHolder, fioDto)) oldNames.add(fioDto);
            if (oldNames.isEmpty()) familyMemberDto.setFioDtos(null);
            else familyMemberDto.setFioDtos(oldNames);
        }
    }

    public boolean checkEmail(String email) {
        if (commonWordChecks.checkForBlanks(email)!=null) return (emailPattern.matcher(email).matches());
        else return false;
    }

    public boolean checkPhone(String phone) {
        if (commonWordChecks.checkForBlanks(phone)!=null) return (phonePattern.matcher(phone).matches());
        else return false;
    }

    public boolean checkFio(TranscriterHolder transcriterHolder, FioDto fioDto) {
        boolean enable = false;
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(commonWordChecks.checkForBlanks(fioDto.getFirstName()));
            if (fioDto.getFirstName() != null) {
                fioDto.setFirstName(commonWordChecks.checkForMulti(transcriterHolder, fioDto.getFirstName(),false));
                enable = true;
            }
        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(commonWordChecks.checkForBlanks(fioDto.getMiddleName()));
            if (fioDto.getMiddleName() != null) {
                fioDto.setMiddleName(commonWordChecks.checkForMulti(transcriterHolder, fioDto.getMiddleName(),false));
                enable = true;
            }
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(commonWordChecks.checkForBlanks(fioDto.getLastName()));
            if (fioDto.getLastName() != null) {
                fioDto.setLastName(commonWordChecks.checkForMulti(transcriterHolder, fioDto.getLastName(),false));
                enable = true;
            }
        }
        if (fioDto.getId() != null) enable = true;
        if (fioDto.getUuid() != null) enable = true;
        System.out.println(fioDto);
        return enable;
    }

    public boolean checkPlace(TranscriterHolder transcriterHolder, PlaceDto placeDto) {
        boolean enable = false;
        if (placeDto.getCountry() != null) {
            placeDto.setCountry(commonWordChecks.checkForBlanks(placeDto.getCountry()));
            if (placeDto.getCountry() != null) {
                placeDto.setCountry(commonWordChecks.checkForMulti(transcriterHolder, placeDto.getCountry(),true));
                enable = true;
            }
        }
        if (placeDto.getRegion() != null) {
            placeDto.setRegion(commonWordChecks.checkForBlanks(placeDto.getRegion()));
            if (placeDto.getRegion() != null) {
                placeDto.setRegion(commonWordChecks.checkForMulti(transcriterHolder, placeDto.getRegion(),true));
                enable = true;
            }
        }
        if (placeDto.getCity() != null) {
            placeDto.setCity(commonWordChecks.checkForBlanks(placeDto.getCity()));
            if (placeDto.getCity() != null) {
                placeDto.setCity(commonWordChecks.checkForMulti(transcriterHolder, placeDto.getCity(),true));
                enable = true;
            }
        }
        if (placeDto.getStreet() != null) {
            placeDto.setStreet(commonWordChecks.checkForBlanks(placeDto.getStreet()));
            if (placeDto.getStreet() != null) {
                placeDto.setStreet(commonWordChecks.checkForMulti(transcriterHolder, placeDto.getStreet(),true));
                enable = true;
            }
        }
        return enable;
    }
    public void checkAddressFM(TranscriterHolder transcriterHolder, AddressDto addressDto){
        if (addressDto.getHouse() != null) {
            addressDto.setHouse(commonWordChecks.checkForBlanks(addressDto.getHouse()));
            if (addressDto.getHouse() != null) {
                addressDto.setHouse(commonWordChecks.checkForMulti(transcriterHolder, addressDto.getHouse(),true));
                }

    }
        if (addressDto.getFlat() != null) {
            addressDto.setFlat(commonWordChecks.checkForBlanks(addressDto.getFlat()));
            if (addressDto.getFlat() != null) {
                addressDto.setFlat(commonWordChecks.checkForMulti(transcriterHolder, addressDto.getFlat(),true));
            }

        }
        if (addressDto.getBuilding() != null) {
            addressDto.setBuilding(commonWordChecks.checkForBlanks(addressDto.getBuilding()));
            if (addressDto.getBuilding() != null) {
                addressDto.setBuilding(commonWordChecks.checkForMulti(transcriterHolder, addressDto.getBuilding(),true));
            }

        }
        if (addressDto.getIndex() != null) {
            addressDto.setIndex(commonWordChecks.checkForBlanks(addressDto.getIndex()));
            if (addressDto.getIndex() != null) {
                addressDto.setIndex(commonWordChecks.checkForMulti(transcriterHolder, addressDto.getIndex(),true));
            }
        }
    }
    public void checkBurial(TranscriterHolder transcriterHolder, BurialDto burialDto){
        if (burialDto.getGrave() != null) {
            burialDto.setGrave(commonWordChecks.checkForBlanks(burialDto.getGrave()));
            if (burialDto.getGrave() != null) {
                burialDto.setGrave(commonWordChecks.checkForMulti(transcriterHolder, burialDto.getGrave(),true));
            }
        }
        if (burialDto.getSquare() != null) {
            burialDto.setSquare(commonWordChecks.checkForBlanks(burialDto.getSquare()));
            if (burialDto.getSquare() != null) {
                burialDto.setSquare(commonWordChecks.checkForMulti(transcriterHolder, burialDto.getSquare(),true));
            }
        }
        if (burialDto.getCemetery() != null) {
            burialDto.setCemetery(commonWordChecks.checkForBlanks(burialDto.getCemetery()));
            if (burialDto.getCemetery() != null) {
                burialDto.setCemetery(commonWordChecks.checkForMulti(transcriterHolder, burialDto.getCemetery(),true));
            }
        }
        if (burialDto.getChapter() != null) {
            burialDto.setChapter(commonWordChecks.checkForBlanks(burialDto.getChapter()));
            if (burialDto.getChapter() != null) {
                burialDto.setChapter(commonWordChecks.checkForMulti(transcriterHolder, burialDto.getChapter(),true));
            }
        }
    }
    public void checkBirth(TranscriterHolder transcriterHolder, BirthDto birthDto){
        if (birthDto.getBirthHouse() != null) {
            birthDto.setBirthHouse(commonWordChecks.checkForBlanks(birthDto.getBirthHouse()));
            if (birthDto.getBirthHouse() != null) {
                birthDto.setBirthHouse(commonWordChecks.checkForMulti(transcriterHolder, birthDto.getBirthHouse(),true));
            }
        }
        if (birthDto.getRegistration() != null) {
            birthDto.setRegistration(commonWordChecks.checkForBlanks(birthDto.getRegistration()));
            if (birthDto.getRegistration() != null) {
                birthDto.setRegistration(commonWordChecks.checkForMulti(transcriterHolder, birthDto.getRegistration(),true));
            }
        }
    }
}

