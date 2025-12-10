package com.example.checks;

import com.example.dtos.*;
import com.example.transcriters.AbstractTranscripter;
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

    public void check(AbstractTranscripter transcripter, FamilyMemberDto familyMemberDto) {
        checkFio(transcripter, familyMemberDto);
        if (familyMemberDto.getMotherFio() != null)
            if (!checkFio(transcripter, familyMemberDto.getMotherFio())) familyMemberDto.setMotherFio(null);
        if (familyMemberDto.getFatherFio() != null)
            if (!checkFio(transcripter, familyMemberDto.getFatherFio())) familyMemberDto.setFatherFio(null);
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
                    if (checkPlace(transcripter, address)) {
                        checkAddressFM(transcripter, address);
                        addressDtos.add(address);
                    }
                }
                if (addressDtos.isEmpty()) familyMemberDto.getMemberInfo().setAddresses(null);
                else familyMemberDto.getMemberInfo().setAddresses(addressDtos);
            }
            if (familyMemberDto.getMemberInfo().getBurial() != null)
                if (!checkPlace(transcripter, familyMemberDto.getMemberInfo().getBurial())) familyMemberDto.getMemberInfo().setBurial(null);
                else checkBurial(transcripter,familyMemberDto.getMemberInfo().getBurial());
            if (familyMemberDto.getMemberInfo().getBirth() != null)
                if (!checkPlace(transcripter, familyMemberDto.getMemberInfo().getBirth())) familyMemberDto.getMemberInfo().setBirth(null);
                else checkBirth(transcripter, familyMemberDto.getMemberInfo().getBirth());
        }


        if (familyMemberDto.getFioDtos() != null) {
            Set<FioDto> oldNames = new HashSet<>();
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                if (checkFio(transcripter, fioDto)) oldNames.add(fioDto);
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

    public boolean checkFio(AbstractTranscripter transcripter, FioDto fioDto) {
        boolean enable = false;
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(commonWordChecks.checkForBlanks(fioDto.getFirstName()));
            if (fioDto.getFirstName() != null) {
                fioDto.setFirstName(commonWordChecks.checkForMulti(transcripter, fioDto.getFirstName(),false));
                enable = true;
            }
        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(commonWordChecks.checkForBlanks(fioDto.getMiddleName()));
            if (fioDto.getMiddleName() != null) {
                fioDto.setMiddleName(commonWordChecks.checkForMulti(transcripter, fioDto.getMiddleName(),false));
                enable = true;
            }
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(commonWordChecks.checkForBlanks(fioDto.getLastName()));
            if (fioDto.getLastName() != null) {
                fioDto.setLastName(commonWordChecks.checkForMulti(transcripter, fioDto.getLastName(),false));
                enable = true;
            }
        }
        if (fioDto.getId() != null) enable = true;
        if (fioDto.getUuid() != null) enable = true;
        System.out.println(fioDto);
        return enable;
    }

    public boolean checkPlace(AbstractTranscripter transcripter, PlaceDto placeDto) {
        boolean enable = false;
        if (placeDto.getCountry() != null) {
            placeDto.setCountry(commonWordChecks.checkForBlanks(placeDto.getCountry()));
            if (placeDto.getCountry() != null) {
                placeDto.setCountry(commonWordChecks.checkForMulti(transcripter, placeDto.getCountry(),true));
                enable = true;
            }
        }
        if (placeDto.getRegion() != null) {
            placeDto.setRegion(commonWordChecks.checkForBlanks(placeDto.getRegion()));
            if (placeDto.getRegion() != null) {
                placeDto.setRegion(commonWordChecks.checkForMulti(transcripter, placeDto.getRegion(),true));
                enable = true;
            }
        }
        if (placeDto.getCity() != null) {
            placeDto.setCity(commonWordChecks.checkForBlanks(placeDto.getCity()));
            if (placeDto.getCity() != null) {
                placeDto.setCity(commonWordChecks.checkForMulti(transcripter, placeDto.getCity(),true));
                enable = true;
            }
        }
        if (placeDto.getStreet() != null) {
            placeDto.setStreet(commonWordChecks.checkForBlanks(placeDto.getStreet()));
            if (placeDto.getStreet() != null) {
                placeDto.setStreet(commonWordChecks.checkForMulti(transcripter, placeDto.getStreet(),true));
                enable = true;
            }
        }
        return enable;
    }
    public void checkAddressFM(AbstractTranscripter transcripter, AddressDto addressDto){
        if (addressDto.getHouse() != null) {
            addressDto.setHouse(commonWordChecks.checkForBlanks(addressDto.getHouse()));
            if (addressDto.getHouse() != null) {
                addressDto.setHouse(commonWordChecks.checkForMulti(transcripter, addressDto.getHouse(),true));
                }

    }
        if (addressDto.getFlatNumber() != null) {
            addressDto.setFlatNumber(commonWordChecks.checkForBlanks(addressDto.getFlatNumber()));
            if (addressDto.getFlatNumber() != null) {
                addressDto.setFlatNumber(commonWordChecks.checkForMulti(transcripter, addressDto.getFlatNumber(),true));
            }

        }
        if (addressDto.getBuilding() != null) {
            addressDto.setBuilding(commonWordChecks.checkForBlanks(addressDto.getBuilding()));
            if (addressDto.getBuilding() != null) {
                addressDto.setBuilding(commonWordChecks.checkForMulti(transcripter, addressDto.getBuilding(),true));
            }

        }
        if (addressDto.getIndex() != null) {
            addressDto.setIndex(commonWordChecks.checkForBlanks(addressDto.getIndex()));
            if (addressDto.getIndex() != null) {
                addressDto.setIndex(commonWordChecks.checkForMulti(transcripter, addressDto.getIndex(),true));
            }
        }
    }
    public void checkBurial(AbstractTranscripter transcripter, BurialDto burialDto){
        if (burialDto.getGrave() != null) {
            burialDto.setGrave(commonWordChecks.checkForBlanks(burialDto.getGrave()));
            if (burialDto.getGrave() != null) {
                burialDto.setGrave(commonWordChecks.checkForMulti(transcripter, burialDto.getGrave(),true));
            }
        }
        if (burialDto.getSquare() != null) {
            burialDto.setSquare(commonWordChecks.checkForBlanks(burialDto.getSquare()));
            if (burialDto.getSquare() != null) {
                burialDto.setSquare(commonWordChecks.checkForMulti(transcripter, burialDto.getSquare(),true));
            }
        }
        if (burialDto.getCemetery() != null) {
            burialDto.setCemetery(commonWordChecks.checkForBlanks(burialDto.getCemetery()));
            if (burialDto.getCemetery() != null) {
                burialDto.setCemetery(commonWordChecks.checkForMulti(transcripter, burialDto.getCemetery(),true));
            }
        }
        if (burialDto.getChapter() != null) {
            burialDto.setChapter(commonWordChecks.checkForBlanks(burialDto.getChapter()));
            if (burialDto.getChapter() != null) {
                burialDto.setChapter(commonWordChecks.checkForMulti(transcripter, burialDto.getChapter(),true));
            }
        }
    }
    public void checkBirth(AbstractTranscripter transcripter, BirthDto birthDto){
        if (birthDto.getBirthHouse() != null) {
            birthDto.setBirthHouse(commonWordChecks.checkForBlanks(birthDto.getBirthHouse()));
            if (birthDto.getBirthHouse() != null) {
                birthDto.setBirthHouse(commonWordChecks.checkForMulti(transcripter, birthDto.getBirthHouse(),true));
            }
        }
        if (birthDto.getRegistration() != null) {
            birthDto.setRegistration(commonWordChecks.checkForBlanks(birthDto.getRegistration()));
            if (birthDto.getRegistration() != null) {
                birthDto.setRegistration(commonWordChecks.checkForMulti(transcripter, birthDto.getRegistration(),true));
            }
        }
    }
}

