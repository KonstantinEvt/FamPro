package ru.memman.services;

import ru.memman.holders.AbstractTextHolder;
import ru.memman.holders.HolderSelector;
import ru.memman.models.ContentDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class TextSelector {
    private HolderSelector holderSelector;

    public ContentDto getContentDto(String localisation) {
        ContentDto contentDto = new ContentDto();
        AbstractTextHolder textHolder = holderSelector.chooseLocalisation(localisation);

            contentDto.setLangMatrix(textHolder.getLangMatrix());
            contentDto.setLocalMenu(textHolder.getLocalMenu());
            contentDto.setGlobalMenu(textHolder.getGlobalMenu());
            contentDto.setGlobalTexts(textHolder.getGlobalTexts());
        return contentDto;
    }


}
