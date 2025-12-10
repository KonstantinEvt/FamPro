package com.example.config;

import com.example.holders.TranscriptHolder;
import com.example.transcriters.AbstractTranscripter;
import com.example.transcriters.EnglishTranscripter;
import com.example.transcriters.RusTranscripter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskResource {
    @Bean("rusTranscript")
    AbstractTranscripter rusTranscript(){return new RusTranscripter();}
    @Bean("englishTranscript")
    AbstractTranscripter englishTranscript(){return new EnglishTranscripter();}

    @Bean("transcriptHolder")
//    @Scope(scopeName = SCOPE_PROTOTYPE,proxyMode = ScopedProxyMode.TARGET_CLASS)
    TranscriptHolder transcriptHolder() {
        return new TranscriptHolder(rusTranscript(), englishTranscript());
    }

}
