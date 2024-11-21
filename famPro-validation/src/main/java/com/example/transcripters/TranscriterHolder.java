package com.example.transcripters;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TranscriterHolder {
    private AbstractTranscriter transctriter = new RusTranscriter();

    public void setTranscriter(String localisation) {
        if (localisation.equals("loc=ru")) transctriter = new RusTranscriter();
    }
}
