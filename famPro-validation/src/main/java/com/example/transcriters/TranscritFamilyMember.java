package com.example.transcriters;

import com.example.checks.CommonWordChecks;
import com.example.dtos.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TranscritFamilyMember {
CommonWordChecks commonWordChecks;

    public void to(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto) {
        transcritToFio(transcriterHolder, familyMemberDto);

        if (familyMemberDto.getMotherFio() != null)
            transcritToFio(transcriterHolder, familyMemberDto.getMotherFio());
        if (familyMemberDto.getFatherFio() != null)
            transcritToFio(transcriterHolder, familyMemberDto.getFatherFio());
        if (familyMemberDto.getFioDtos() != null) {
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                transcritToFio(transcriterHolder, fioDto);
        }
    }

    public void toGet(TranscriterHolder transcriterHolder, FamilyMemberDto familyMemberDto) {
        familyMemberDto.setFullName(parseFullName(transcriterHolder, familyMemberDto.getFullName()));
        familyMemberDto.setMotherInfo(parseFullName(transcriterHolder, familyMemberDto.getMotherInfo()));
        familyMemberDto.setFatherInfo(parseFullName(transcriterHolder, familyMemberDto.getFatherInfo()));
        if (familyMemberDto.getMemberInfo() == null) familyMemberDto.setMemberInfo(new FamilyMemberInfoDto());
        if (familyMemberDto.getMemberInfo().getMainEmail() == null)
            familyMemberDto.getMemberInfo().setMainEmail(transcriterHolder.getTranscriter().getOut());
        if (familyMemberDto.getMemberInfo().getMainPhone() == null)
            familyMemberDto.getMemberInfo().setMainPhone(transcriterHolder.getTranscriter().getOut());
        familyMemberDto.getMemberInfo().setMainAddress(parseFullAddress(transcriterHolder, familyMemberDto.getMemberInfo().getMainAddress()));
    }

    private String parseFullAddress(TranscriterHolder transcriterHolder, String str) {
        AbstractTranscriter trans = transcriterHolder.getTranscriter();
        if (str == null || str.isEmpty()) return trans.getOut();
        else return trans.transcritWordToLocalisation(str);
    }

    private String parseFullName(TranscriterHolder transcriterHolder, String str) {
        String result;
        AbstractTranscriter trans = transcriterHolder.getTranscriter();
        if (str == null || str.isEmpty()) return trans.getOut();
        if (str.charAt(0) == '(') {
            if (str.charAt(1) == 'A') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                result = String.join(" ", trans.getAbsent(), trans.transcritWordToLocalisation(strings[0]),
                        trans.transcritWordToLocalisation(strings[1]),
                        trans.transcritWordToLocalisation(strings[2]),
                        trans.getBirthdayString(), strings[6]);
            } else if (str.charAt(1) == 'I') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                result = String.join(" ", trans.getInfoNotFully(), strings[0].equals("null") ? trans.empty() : trans.transcritWordToLocalisation(strings[0]),
                        strings[1].equals("null") ? trans.empty() : trans.transcritWordToLocalisation(strings[1]),
                        strings[2].equals("null") ? trans.empty() : trans.transcritWordToLocalisation(strings[2]),
                        trans.getBirthdayString(),
                        strings[6].equals("null") ? trans.empty() : strings[6]);
            } else result = trans.getIncorrectInfo();
        } else {
            String[] strings = str.split(" ");
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
        if (familyMemberDto.getMemberInfo() != null && familyMemberDto.getMemberInfo().getAddresses() != null)
            for (AddressDto address : familyMemberDto.getMemberInfo().getAddresses()) {
                transcritFromAddress(transcriterHolder, address);
                System.out.println(familyMemberDto);
            }
        if (familyMemberDto.getBirth() != null) transcritFromBirth(transcriterHolder, familyMemberDto.getBirth());
        if (familyMemberDto.getBurial() != null) transcritFromBurial(transcriterHolder, familyMemberDto.getBurial());
    }

    public void transcritFromAddress(TranscriterHolder transcriterHolder, AddressDto address) {
        if (address.getHouse() != null)
            address.setHouse(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getHouse()));
        if (address.getIndex() != null)
            address.setIndex(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getIndex()));
        if (address.getBuilding() != null)
            address.setBuilding(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getBuilding()));
        if (address.getFlat() != null)
            address.setFlat(transcriterHolder.getTranscriter().transcritWordFromLocalisation(address.getFlat()));
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
}