package com.example.service;

import com.example.checks.CommonWordChecks;
import com.example.dtos.*;
import com.example.enums.SecretLevel;
import com.example.transcriters.AbstractTranscripter;
import com.ibm.icu.text.SimpleDateFormat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.util.Objects;

@Component
@AllArgsConstructor
public class TranscritFamilyMember {
    CommonWordChecks commonWordChecks;

    public void to(AbstractTranscripter transcripter, FamilyMemberDto familyMemberDto) {
        transcritToFio(transcripter, familyMemberDto);

//        if (familyMemberDto.getMotherFio() != null)
//            transcritToFio(transcripter, familyMemberDto.getMotherFio());
//        if (familyMemberDto.getFatherFio() != null)
//            transcritToFio(transcripter, familyMemberDto.getFatherFio());
        toGetOtherNames(transcripter, familyMemberDto);
    }

    public void toGetOtherNames(AbstractTranscripter transcripter, FamilyMemberDto familyMemberDto) {
        if (familyMemberDto.getFioDtos() != null) {
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                transcritToFio(transcripter, fioDto);
        }
    }

    public void toGet(AbstractTranscripter transcripter, FamilyMemberDto familyMemberDto) throws ParseException {
        boolean secret = familyMemberDto.getSecretLevelBirthday() == SecretLevel.CLOSE;
        familyMemberDto.setFullName(parseFullName(transcripter, familyMemberDto.getFullName(), new FioDto(), secret));
        if (secret) familyMemberDto.setBirthday(null);
        FioDto fatherFio = new FioDto();
        FioDto motherFio = new FioDto();
        familyMemberDto.setMotherInfo(parseFullName(transcripter, familyMemberDto.getMotherInfo(), motherFio, secret));
        familyMemberDto.setFatherInfo(parseFullName(transcripter, familyMemberDto.getFatherInfo(), fatherFio, secret));
        if (!Objects.equals(familyMemberDto.getMotherInfo(), transcripter.getOut()) || !Objects.equals(familyMemberDto.getMotherInfo(), transcripter.getIncorrectInfo()))
            familyMemberDto.setMotherFio(motherFio);
        if (!Objects.equals(familyMemberDto.getFatherInfo(), transcripter.getOut()) || !Objects.equals(familyMemberDto.getFatherInfo(), transcripter.getIncorrectInfo()))
            familyMemberDto.setFatherFio(fatherFio);
        if (familyMemberDto.getMemberInfo() == null) familyMemberDto.setMemberInfo(new FamilyMemberInfoDto());
        if (familyMemberDto.getMemberInfo().getMainEmail() == null)
            familyMemberDto.getMemberInfo().setMainEmail(transcripter.getOut());
        if (familyMemberDto.getMemberInfo().getMainPhone() == null)
            familyMemberDto.getMemberInfo().setMainPhone(transcripter.getOut());
        familyMemberDto.getMemberInfo().setMainAddress(parseFullAddressTo(transcripter, familyMemberDto.getMemberInfo().getMainAddress()));
    }

    public void toGetInfo(AbstractTranscripter transcripter, FamilyMemberDto familyMemberDto) {
        if (familyMemberDto.getMemberInfo() != null) {
            if (familyMemberDto.getMemberInfo().getAddresses() != null) {
                for (AddressDto address : familyMemberDto.getMemberInfo().getAddresses()) {
                    transcritToAddress(transcripter, address);
                    address.setInternName(parseFullAddressTo(transcripter, address.getInternName()));
                }
            }
            if (familyMemberDto.getMemberInfo().getBirth() != null) {
                transcritToBirth(transcripter, familyMemberDto.getMemberInfo().getBirth());
                familyMemberDto.getMemberInfo().getBirth().setInternName(parseFullAddressTo(transcripter, familyMemberDto.getMemberInfo().getBirth().getInternName()));
            }
            if (familyMemberDto.getMemberInfo().getBurial() != null) {
                transcritToBurial(transcripter, familyMemberDto.getMemberInfo().getBurial());
                familyMemberDto.getMemberInfo().getBurial().setInternName(parseFullAddressTo(transcripter, familyMemberDto.getMemberInfo().getBurial().getInternName()));
            }

        }
    }

    private String parseFullAddressTo(AbstractTranscripter trans, String str) {
        if (str == null || str.isEmpty()) return trans.getOut();
        else return trans.transcritWordToLocalisation(str);
    }

    public String parseFullName(AbstractTranscripter trans, String str, FioDto fioDto, boolean secret) throws ParseException {
        String result;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        if (str == null || str.isEmpty()) return trans.getOut();
        if (str.charAt(0) == '(') {
            if (str.charAt(1) == 'A') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                fioDto.setFirstName(trans.transcritWordToLocalisation(strings[0]));
                fioDto.setMiddleName(trans.transcritWordToLocalisation(strings[1]));
                fioDto.setLastName(trans.transcritWordToLocalisation(strings[2]));
                if (!secret) fioDto.setBirthday(new Date(format.parse(strings[6]).getTime()));
                else strings[6] = trans.getClose();
                result = String.join(" ", trans.getAbsent(), fioDto.getFirstName(),
                        fioDto.getMiddleName(),
                        fioDto.getLastName(),
                        trans.getBirthdayString(), strings[6]);
            } else if (str.charAt(1) == 'I') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                if (!strings[0].equals("null")) fioDto.setFirstName(trans.transcritWordToLocalisation(strings[0]));
                if (!strings[1].equals("null")) fioDto.setFirstName(trans.transcritWordToLocalisation(strings[1]));
                if (!strings[2].equals("null")) fioDto.setFirstName(trans.transcritWordToLocalisation(strings[2]));
                if (!strings[6].equals("null"))
                    if (!secret) fioDto.setBirthday(new Date(format.parse(strings[6]).getTime()));
                    else strings[6] = trans.getClose();
                result = String.join(" ", trans.getInfoNotFully(), strings[0].equals("null") ? trans.empty() : fioDto.getFirstName(),
                        strings[1].equals("null") ? trans.empty() : fioDto.getMiddleName(),
                        strings[2].equals("null") ? trans.empty() : fioDto.getLastName(),
                        trans.getBirthdayString(),
                        strings[6].equals("null") ? trans.empty() : strings[6]);
            } else result = trans.getIncorrectInfo();
        } else {
            String[] strings = str.split(" ");
            fioDto.setFirstName(trans.transcritWordToLocalisation(strings[0]));
            fioDto.setMiddleName(trans.transcritWordToLocalisation(strings[1]));
            fioDto.setLastName(trans.transcritWordToLocalisation(strings[2]));
            if (!secret) fioDto.setBirthday(new Date(format.parse(strings[6]).getTime()));
            else strings[6] = trans.getClose();
            result = String.join(" ", trans.transcritWordToLocalisation(strings[0]),
                    trans.transcritWordToLocalisation(strings[1]),
                    trans.transcritWordToLocalisation(strings[2]),
                    trans.getBirthdayString(), strings[6]);
        }
        return result;
    }

    public void transcritToFio(AbstractTranscripter transcripter, FioDto fioDto) {
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(commonWordChecks.setUpperFirst(transcripter.transcritWordToLocalisation(fioDto.getFirstName())));

        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(commonWordChecks.setUpperFirst(transcripter.transcritWordToLocalisation(fioDto.getMiddleName())));
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(commonWordChecks.setUpperFirst(transcripter.transcritWordToLocalisation(fioDto.getLastName())));
        }
    }

    public void from(AbstractTranscripter transcripter, FamilyMemberDto familyMemberDto) {
        transcritFromFio(transcripter, familyMemberDto);

        if (familyMemberDto.getMotherFio() != null)
            transcritFromFio(transcripter, familyMemberDto.getMotherFio());
        if (familyMemberDto.getFatherFio() != null) transcritFromFio(transcripter, familyMemberDto.getFatherFio());

        if (familyMemberDto.getFioDtos() != null) {
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                transcritFromFio(transcripter, fioDto);
        }
        if (familyMemberDto.getMemberInfo() != null) {
            if (familyMemberDto.getMemberInfo().getAddresses() != null) {
                for (AddressDto address : familyMemberDto.getMemberInfo().getAddresses()) {
                    transcritFromAddress(transcripter, address);
                }
            }
            if (familyMemberDto.getMemberInfo().getBirth() != null)
                transcritFromBirth(transcripter, familyMemberDto.getMemberInfo().getBirth());
            if (familyMemberDto.getMemberInfo().getBurial() != null)
                transcritFromBurial(transcripter, familyMemberDto.getMemberInfo().getBurial());

        }
    }

    public void transcritFromAddress(AbstractTranscripter transcripter, AddressDto address) {
        if (address.getHouse() != null)
            address.setHouse(transcripter.transcritWordFromLocalisation(address.getHouse()));
        if (address.getIndex() != null)
            address.setIndex(transcripter.transcritWordFromLocalisation(address.getIndex()));
        if (address.getBuilding() != null)
            address.setBuilding(transcripter.transcritWordFromLocalisation(address.getBuilding()));
        if (address.getFlatNumber() != null)
            address.setFlatNumber(transcripter.transcritWordFromLocalisation(address.getFlatNumber()));
        transcritFromPlace(transcripter, address);
    }

    public void transcritFromBirth(AbstractTranscripter transcripter, BirthDto birthDto) {
        if (birthDto.getBirthHouse() != null)
            birthDto.setBirthHouse(transcripter.transcritWordFromLocalisation(birthDto.getBirthHouse()));
        if (birthDto.getRegistration() != null)
            birthDto.setRegistration(transcripter.transcritWordFromLocalisation(birthDto.getRegistration()));
        transcritFromPlace(transcripter, birthDto);
    }

    public void transcritFromBurial(AbstractTranscripter transcripter, BurialDto burialDto) {
        if (burialDto.getCemetery() != null)
            burialDto.setCemetery(transcripter.transcritWordFromLocalisation(burialDto.getCemetery()));
        if (burialDto.getChapter() != null)
            burialDto.setChapter(transcripter.transcritWordFromLocalisation(burialDto.getChapter()));
        if (burialDto.getSquare() != null)
            burialDto.setSquare(transcripter.transcritWordFromLocalisation(burialDto.getSquare()));
        if (burialDto.getGrave() != null)
            burialDto.setGrave(transcripter.transcritWordFromLocalisation(burialDto.getGrave()));
        transcritFromPlace(transcripter, burialDto);
    }

    public void transcritFromPlace(AbstractTranscripter transcripter, PlaceDto placeDto) {
        if (placeDto.getCountry() != null)
            placeDto.setCountry(transcripter.transcritWordFromLocalisation(placeDto.getCountry()));
        if (placeDto.getRegion() != null)
            placeDto.setRegion(transcripter.transcritWordFromLocalisation(placeDto.getRegion()));
        if (placeDto.getStreet() != null)
            placeDto.setStreet(transcripter.transcritWordFromLocalisation(placeDto.getStreet()));
        if (placeDto.getCity() != null)
            placeDto.setCity(transcripter.transcritWordFromLocalisation(placeDto.getCity()));
    }


    public void transcritFromFio(AbstractTranscripter transcripter, FioDto fioDto) {
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(transcripter.transcritWordFromLocalisation(fioDto.getFirstName()));
        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(transcripter.transcritWordFromLocalisation(fioDto.getMiddleName()));
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(transcripter.transcritWordFromLocalisation(fioDto.getLastName()));
        }
    }

    public void transcritToAddress(AbstractTranscripter trans, AddressDto address) {
        if (address.getHouse() != null)
            address.setHouse(trans.transcritWordToLocalisation(address.getHouse()));
        if (address.getIndex() != null)
            address.setIndex(trans.transcritWordToLocalisation(address.getIndex()));
        if (address.getBuilding() != null)
            address.setBuilding(trans.transcritWordToLocalisation(address.getBuilding()));
        if (address.getFlatNumber() != null)
            address.setFlatNumber(trans.transcritWordToLocalisation(address.getFlatNumber()));
        transcritToPlace(trans, address);
    }

    public void transcritToBirth(AbstractTranscripter trans, BirthDto birthDto) {
        if (birthDto.getBirthHouse() != null)
            birthDto.setBirthHouse(trans.transcritWordToLocalisation(birthDto.getBirthHouse()));
        if (birthDto.getRegistration() != null)
            birthDto.setRegistration(trans.transcritWordToLocalisation(birthDto.getRegistration()));
        transcritToPlace(trans, birthDto);
    }

    public void transcritToBurial(AbstractTranscripter trans, BurialDto burialDto) {
        if (burialDto.getCemetery() != null)
            burialDto.setCemetery(trans.transcritWordToLocalisation(burialDto.getCemetery()));
        if (burialDto.getChapter() != null)
            burialDto.setChapter(trans.transcritWordToLocalisation(burialDto.getChapter()));
        if (burialDto.getSquare() != null)
            burialDto.setSquare(trans.transcritWordToLocalisation(burialDto.getSquare()));
        if (burialDto.getGrave() != null)
            burialDto.setGrave(trans.transcritWordToLocalisation(burialDto.getGrave()));
        transcritToPlace(trans, burialDto);
    }

    public void transcritToPlace(AbstractTranscripter trans, PlaceDto placeDto) {
        if (placeDto.getCountry() != null)
            placeDto.setCountry(trans.transcritWordToLocalisation(placeDto.getCountry()));
        if (placeDto.getRegion() != null)
            placeDto.setRegion(trans.transcritWordToLocalisation(placeDto.getRegion()));
        if (placeDto.getStreet() != null)
            placeDto.setStreet(trans.transcritWordToLocalisation(placeDto.getStreet()));
        if (placeDto.getCity() != null)
            placeDto.setCity(trans.transcritWordToLocalisation(placeDto.getCity()));
    }
}