package com.example.transcriters;

import com.example.checks.CommonWordChecks;
import com.example.dtos.*;
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

    public void to(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto) {
        transcritToFio(transcriterHolder, familyMemberDto);

//        if (familyMemberDto.getMotherFio() != null)
//            transcritToFio(transcriterHolder, familyMemberDto.getMotherFio());
//        if (familyMemberDto.getFatherFio() != null)
//            transcritToFio(transcriterHolder, familyMemberDto.getFatherFio());
       toGetOtherNames(transcriterHolder,familyMemberDto);
    }
public void toGetOtherNames(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto){
    if (familyMemberDto.getFioDtos() != null) {
        for (FioDto fioDto :
                familyMemberDto.getFioDtos())
            transcritToFio(transcriterHolder, fioDto);
    }
}
    public void toGet(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto) throws ParseException {
        AbstractTranscriter trans= transcriterHolder.getTranscriter();
        familyMemberDto.setFullName(parseFullName(trans, familyMemberDto.getFullName(), new FioDto()));
        FioDto fatherFio = new FioDto();
        FioDto motherFio = new FioDto();
        familyMemberDto.setMotherInfo(parseFullName(trans, familyMemberDto.getMotherInfo(), motherFio));
        familyMemberDto.setFatherInfo(parseFullName(trans, familyMemberDto.getFatherInfo(), fatherFio));
        if (!Objects.equals(familyMemberDto.getMotherInfo(), trans.getOut()) || !Objects.equals(familyMemberDto.getMotherInfo(), trans.getIncorrectInfo()))
            familyMemberDto.setMotherFio(motherFio);
        if (!Objects.equals(familyMemberDto.getFatherInfo(), trans.getOut()) || !Objects.equals(familyMemberDto.getFatherInfo(), trans.getIncorrectInfo()))
            familyMemberDto.setFatherFio(fatherFio);
        if (familyMemberDto.getMemberInfo() == null) familyMemberDto.setMemberInfo(new FamilyMemberInfoDto());
        if (familyMemberDto.getMemberInfo().getMainEmail() == null)
            familyMemberDto.getMemberInfo().setMainEmail(trans.getOut());
        if (familyMemberDto.getMemberInfo().getMainPhone() == null)
            familyMemberDto.getMemberInfo().setMainPhone(trans.getOut());
        familyMemberDto.getMemberInfo().setMainAddress(parseFullAddressTo(trans, familyMemberDto.getMemberInfo().getMainAddress()));
    }
public void toGetInfo(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto){
    AbstractTranscriter trans = transcriterHolder.getTranscriter();
    if (familyMemberDto.getMemberInfo() != null) {
        if (familyMemberDto.getMemberInfo().getAddresses() != null) {
            for (AddressDto address : familyMemberDto.getMemberInfo().getAddresses()) {
                transcritToAddress(trans, address);
                address.setInternName(parseFullAddressTo(trans,address.getInternName()));
            }
        }
        if (familyMemberDto.getMemberInfo().getBirth() != null) {
            transcritToBirth(trans, familyMemberDto.getMemberInfo().getBirth());
            familyMemberDto.getMemberInfo().getBirth().setInternName(parseFullAddressTo(trans,familyMemberDto.getMemberInfo().getBirth().getInternName()));
        }
        if (familyMemberDto.getMemberInfo().getBurial() != null) {
            transcritToBurial(trans, familyMemberDto.getMemberInfo().getBurial());
            familyMemberDto.getMemberInfo().getBurial().setInternName(parseFullAddressTo(trans,familyMemberDto.getMemberInfo().getBurial().getInternName()));
        }

    }
}
    private String parseFullAddressTo(AbstractTranscriter trans, String str) {
        if (str == null || str.isEmpty()) return trans.getOut();
        else return trans.transcritWordToLocalisation(str);
    }

    private String parseFullName(AbstractTranscriter trans, String str, FioDto fioDto) throws ParseException {
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
                fioDto.setBirthday(new Date(format.parse(strings[6]).getTime()));
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
                if (!strings[6].equals("null")) fioDto.setBirthday(new Date(format.parse(strings[6]).getTime()));
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
            fioDto.setBirthday(new Date(format.parse(strings[6]).getTime()));
            result = String.join(" ", trans.transcritWordToLocalisation(strings[0]),
                    trans.transcritWordToLocalisation(strings[1]),
                    trans.transcritWordToLocalisation(strings[2]),
                    trans.getBirthdayString(), strings[6]);
        }
        return result;
    }

    public void transcritToFio(TranscriterHolder transcriterHolder, FioDto fioDto) {
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(commonWordChecks.setUpperFirst(transcriterHolder.getTranscriter().transcritWordToLocalisation(fioDto.getFirstName())));

        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(commonWordChecks.setUpperFirst(transcriterHolder.getTranscriter().transcritWordToLocalisation(fioDto.getMiddleName())));
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(commonWordChecks.setUpperFirst(transcriterHolder.getTranscriter().transcritWordToLocalisation(fioDto.getLastName())));
        }
    }

    public void from(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto) {
        transcritFromFio(transcriterHolder, familyMemberDto);

        if (familyMemberDto.getMotherFio() != null)
            transcritFromFio(transcriterHolder, familyMemberDto.getMotherFio());
        if (familyMemberDto.getFatherFio() != null) transcritFromFio(transcriterHolder, familyMemberDto.getFatherFio());

        if (familyMemberDto.getFioDtos() != null) {
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                transcritFromFio(transcriterHolder, fioDto);
        }
        if (familyMemberDto.getMemberInfo() != null) {
            if (familyMemberDto.getMemberInfo().getAddresses() != null) {
                for (AddressDto address : familyMemberDto.getMemberInfo().getAddresses()) {
                    transcritFromAddress(transcriterHolder, address);
                }
            }
            if (familyMemberDto.getMemberInfo().getBirth() != null)
                transcritFromBirth(transcriterHolder, familyMemberDto.getMemberInfo().getBirth());
            if (familyMemberDto.getMemberInfo().getBurial() != null)
                transcritFromBurial(transcriterHolder, familyMemberDto.getMemberInfo().getBurial());

        }
    }

    public void transcritFromAddress(TranscriterHolder transcriterHolder, AddressDto address) {
        if (address.getHouse() != null)
            address.setHouse(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getHouse()));
        if (address.getIndex() != null)
            address.setIndex(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getIndex()));
        if (address.getBuilding() != null)
            address.setBuilding(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getBuilding()));
        if (address.getFlatNumber() != null)
            address.setFlatNumber(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getFlatNumber()));
        transcritFromPlace(transcriterHolder, address);
    }

    public void transcritFromBirth(TranscriterHolder transcriterHolder, BirthDto birthDto) {
        if (birthDto.getBirthHouse() != null)
            birthDto.setBirthHouse(transcriterHolder.getTranscriter().transcritWordFromLocalisation(birthDto.getBirthHouse()));
        if (birthDto.getRegistration() != null)
            birthDto.setRegistration(transcriterHolder.getTranscriter().transcritWordFromLocalisation(birthDto.getRegistration()));
        transcritFromPlace(transcriterHolder, birthDto);
    }

    public void transcritFromBurial(TranscriterHolder transcriterHolder, BurialDto burialDto) {
        if (burialDto.getCemetery() != null)
            burialDto.setCemetery(transcriterHolder.getTranscriter().transcritWordFromLocalisation(burialDto.getCemetery()));
        if (burialDto.getChapter() != null)
            burialDto.setChapter(transcriterHolder.getTranscriter().transcritWordFromLocalisation(burialDto.getChapter()));
        if (burialDto.getSquare() != null)
            burialDto.setSquare(transcriterHolder.getTranscriter().transcritWordFromLocalisation(burialDto.getSquare()));
        if (burialDto.getGrave() != null)
            burialDto.setGrave(transcriterHolder.getTranscriter().transcritWordFromLocalisation(burialDto.getGrave()));
        transcritFromPlace(transcriterHolder, burialDto);
    }

    public void transcritFromPlace(TranscriterHolder transcriterHolder, PlaceDto placeDto) {
        if (placeDto.getCountry() != null)
            placeDto.setCountry(transcriterHolder.getTranscriter().transcritWordFromLocalisation(placeDto.getCountry()));
        if (placeDto.getRegion() != null)
            placeDto.setRegion(transcriterHolder.getTranscriter().transcritWordFromLocalisation(placeDto.getRegion()));
        if (placeDto.getStreet() != null)
            placeDto.setStreet(transcriterHolder.getTranscriter().transcritWordFromLocalisation(placeDto.getStreet()));
        if (placeDto.getCity() != null)
            placeDto.setCity(transcriterHolder.getTranscriter().transcritWordFromLocalisation(placeDto.getCity()));
    }


    public void transcritFromFio(TranscriterHolder transcriterHolder, FioDto fioDto) {
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(transcriterHolder.getTranscriter().transcritWordFromLocalisation(fioDto.getFirstName()));
        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(transcriterHolder.getTranscriter().transcritWordFromLocalisation(fioDto.getMiddleName()));
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(transcriterHolder.getTranscriter().transcritWordFromLocalisation(fioDto.getLastName()));
        }
    }
    public void transcritToAddress(AbstractTranscriter trans, AddressDto address) {
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

    public void transcritToBirth(AbstractTranscriter trans, BirthDto birthDto) {
        if (birthDto.getBirthHouse() != null)
            birthDto.setBirthHouse(trans.transcritWordToLocalisation(birthDto.getBirthHouse()));
        if (birthDto.getRegistration() != null)
            birthDto.setRegistration(trans.transcritWordToLocalisation(birthDto.getRegistration()));
        transcritToPlace(trans, birthDto);
    }

    public void transcritToBurial(AbstractTranscriter trans, BurialDto burialDto) {
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

    public void transcritToPlace(AbstractTranscriter trans, PlaceDto placeDto) {
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