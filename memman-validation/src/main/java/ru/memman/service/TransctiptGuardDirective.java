package ru.memman.service;

import org.springframework.stereotype.Service;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.FioDto;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.Subject;
import ru.memman.enums.SwitchPosition;
import ru.memman.holders.TranscriptHolder;
import ru.memman.transcriters.AbstractTranscripter;

import java.text.ParseException;

@Service
public class TransctiptGuardDirective {
    private final TranscritFamilyMember transcritFamilyMember;
    private final TranscriptHolder transcriptHolder;
    public TransctiptGuardDirective(TranscritFamilyMember transcritFamilyMember, TranscriptHolder transcriptHolder) {
        this.transcritFamilyMember = transcritFamilyMember;
        this.transcriptHolder = transcriptHolder;
    }

    public void transcriptGuardDirective(DirectiveGuards directiveGuards)  {
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(directiveGuards.getLocalisation());
        directiveGuards.setInfo1(parseFullName(transcripter, directiveGuards.getInfo1()));
        if (directiveGuards.getSubject() == Subject.VOTING) {
            if (directiveGuards.getSwitchPosition() == SwitchPosition.MAIN) {
                String[] parentsInfo = directiveGuards.getInfo2().split("!");
                directiveGuards.setInfo2(parseFullName(transcripter, parentsInfo[0]));
                directiveGuards.setInfo3(parseFullName(transcripter, parentsInfo[1]));
            } else
                directiveGuards.setInfo2(parseFullName(transcripter, directiveGuards.getInfo2()));
        }
    }


//    public DirectiveGuards transcriptVoting2(DirectiveGuards directiveGuards) throws ParseException {
//        AbstractTranscripter transcripter = transcriptHolder.getTranscript(directiveGuards.getLocalisation());
//        if (directiveGuards.getSubject() == Subject.VOTING) {
//            String info;
//            if (directiveGuards.getSwitchPosition() == SwitchPosition.MAIN) {
//                String[] parentsInfo = directiveGuards.getInfo2().split("<br>");
//                info = transcritFamilyMember.parseFullName(transcripter, parentsInfo[0], new FioDto(), true)
//                        .concat(transcripter.getAnd())
//                        .concat(transcritFamilyMember.parseFullName(transcripter, parentsInfo[1], new FioDto(), true));
//
//            } else
//                info = transcritFamilyMember.parseFullName(transcripter, directiveGuards.getInfo2(), new FioDto(), true);
//            directiveGuards.setInfo2(transcripter.getMatrixOfTextGeneration().get(directiveGuards.getSubject().name())
//                    .concat("<br>")
//                    .concat(transcritFamilyMember.parseFullName(transcripter, directiveGuards.getInfo1(), new FioDto(), false))
//                    .concat("<br>")
//                    .concat(transcripter.getTextSwitchPosition(directiveGuards.getSwitchPosition()))
//                    .concat("<br>")
//                    .concat(info)
//                    .concat("<br>")
//                    .concat(transcripter.getMatrixOfTextGeneration().get("USER_NICK"))
//                    .concat("<br>"));
//        } else {
//            directiveGuards.setInfo2(transcripter.getMatrixOfTextGeneration().get(directiveGuards.getSubject().name())
//                    .concat("<br>")
//                    .concat(transcritFamilyMember.parseFullName(transcripter, directiveGuards.getInfo1(), new FioDto(), true))
//                    .concat("<br>")
//                    .concat(transcripter.getMatrixOfTextGeneration().get("USER_NICK"))
//                    .concat("<br>"));
//        }
//
//        directiveGuards.setInfo1(transcripter.getTextSubject(directiveGuards.getSubject()));
//        directiveGuards.setInfo3(directiveGuards.getInfo1());
//
//        return directiveGuards;
//    }

//    public DirectiveGuards transcriptAttention2(DirectiveGuards directiveGuards) throws ParseException {
//        AbstractTranscripter transcripter = transcriptHolder.getTranscript(directiveGuards.getLocalisation());
//        if (directiveGuards.getPerson() != null) {
//
//            String temp = directiveGuards.getInfo1();
//            directiveGuards.setInfo1(transcritFamilyMember.parseFullName(transcripter, directiveGuards.getPerson(), new FioDto(), directiveGuards.getSubject() != Subject.LINK_POSITIVE));
//            directiveGuards.setPerson(temp);
//        }
//
//        return directiveGuards;
//    }
    public String parseFullName(AbstractTranscripter trans, String str) {
        String result;

        if (str == null || str.isEmpty()) return trans.getOut();
        if (str.charAt(0) == '(') {
            if (str.charAt(1) == 'A') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                result = String.join(" ",
                        trans.transcritWordToLocalisation(strings[0]),
                        trans.transcritWordToLocalisation(strings[1]),
                        trans.transcritWordToLocalisation(strings[2]),
                        strings[6]);
            } else if (str.charAt(1) == 'I') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                if (!strings[0].equals(CheckStatus.NUL.name())) strings[0]=trans.transcritWordToLocalisation(strings[0]);
                if (!strings[1].equals(CheckStatus.NUL.name())) strings[1]=trans.transcritWordToLocalisation(strings[1]);
                if (!strings[2].equals(CheckStatus.NUL.name())) strings[2]=trans.transcritWordToLocalisation(strings[2]);
                result = String.join(" ", strings[0],strings[1],strings[2],strings[6]);
            } else result = CheckStatus.UNCORRECTED.name();
        } else {
            String[] strings = str.split(" ");
            result = String.join(" ", trans.transcritWordToLocalisation(strings[0]),
                    trans.transcritWordToLocalisation(strings[1]),
                    trans.transcritWordToLocalisation(strings[2]),
                    strings[6]);
        }
        return result;
    }
}
