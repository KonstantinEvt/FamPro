package ru.memman.holders;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.enums.Localisation;
import ru.memman.exceptions.UncorrectedInformation;
import ru.memman.transcriters.AbstractTranscripter;

import java.util.Objects;

@Getter
@Component
public class TranscriptHolder {
    private final AbstractTranscripter rusTranscript;
    private final AbstractTranscripter englishTranscript;

    public TranscriptHolder(AbstractTranscripter rusTranscript, AbstractTranscripter englishTranscript) {
        this.rusTranscript = rusTranscript;
        this.englishTranscript = englishTranscript;
    }

    public AbstractTranscripter getTranscript(Localisation localisation) {
        switch (localisation) {
            case RU -> {
                return rusTranscript;
            }
            default -> {
                return englishTranscript;
            }
        }


    }}
