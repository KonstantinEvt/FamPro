package ru.memman.config;

import ru.memman.holders.TranscriptHolder;
import ru.memman.transcriters.AbstractTranscripter;
import ru.memman.transcriters.EnglishTranscripter;
import ru.memman.transcriters.RusTranscripter;
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
