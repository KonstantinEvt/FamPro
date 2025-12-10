package com.example.service;

import com.example.checks.CheckFamilyMember;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FioDto;
import com.example.enums.Subject;
import com.example.enums.SwitchPosition;
import com.example.holders.TranscriptHolder;
import com.example.transcriters.AbstractTranscripter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Set;

@Service
@AllArgsConstructor
public class TransctiptGuardDirective {
    private final CheckFamilyMember checkFamilyMember;
    private final TranscritFamilyMember transcritFamilyMember;
    private final TokenService tokenService;
    private final TranscriptHolder transcriptHolder;

    public DirectiveGuards transcriptVoting(DirectiveGuards directiveGuards) throws ParseException {
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(directiveGuards.getLocalisation());
        if (directiveGuards.getSubject() == Subject.VOTING) {
            String info;
            if (directiveGuards.getSwitchPosition() == SwitchPosition.MAIN) {
                String[] parentsInfo = directiveGuards.getInfo2().split("<br>");
                info = transcritFamilyMember.parseFullName(transcripter, parentsInfo[0], new FioDto(), true)
                        .concat(transcripter.getAnd())
                        .concat(transcritFamilyMember.parseFullName(transcripter, parentsInfo[1], new FioDto(), true));

            } else
                info = transcritFamilyMember.parseFullName(transcripter, directiveGuards.getInfo2(), new FioDto(), true);
            directiveGuards.setInfo2(transcripter.getMatrixOfTextGeneration().get(directiveGuards.getSubject().name())
                    .concat("<br>")
                    .concat(transcritFamilyMember.parseFullName(transcripter, directiveGuards.getInfo1(), new FioDto(), false))
                    .concat("<br>")
                    .concat(transcripter.getTextSwitchPosition(directiveGuards.getSwitchPosition()))
                    .concat("<br>")
                    .concat(info)
                    .concat("<br>")
                    .concat(transcripter.getMatrixOfTextGeneration().get("USER_NICK"))
                    .concat("<br>"));
        } else {
            directiveGuards.setInfo2(transcripter.getMatrixOfTextGeneration().get(directiveGuards.getSubject().name())
                    .concat("<br>")
                    .concat(transcritFamilyMember.parseFullName(transcripter, directiveGuards.getInfo1(), new FioDto(), true))
                    .concat("<br>")
                    .concat(transcripter.getMatrixOfTextGeneration().get("USER_NICK"))
                    .concat("<br>"));
        }
        directiveGuards.setInfo1(transcripter.getTextSubject(directiveGuards.getSubject()));
        directiveGuards.setInfo3(directiveGuards.getInfo1());

        return directiveGuards;
    }

    public DirectiveGuards transcriptAttention(DirectiveGuards directiveGuards) throws ParseException {
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(directiveGuards.getLocalisation());
        if (directiveGuards.getPerson() != null)
            directiveGuards.setInfo1(transcritFamilyMember.parseFullName(transcripter, directiveGuards.getPerson(), new FioDto(), true));
        directiveGuards.setInfo3(transcripter.getTextSubject(directiveGuards.getSubject()));
        directiveGuards.setInfo2(transcripter.getMatrixOfTextGeneration().get(directiveGuards.getSubject().name()));
        return directiveGuards;
    }
}
