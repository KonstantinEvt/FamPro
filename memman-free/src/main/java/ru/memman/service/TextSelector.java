package ru.memman.service;

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

    public ContentDto getContentDto(String localisation, int part, boolean full) {
        ContentDto contentDto = new ContentDto();
        AbstractTextHolder textHolder = holderSelector.chooseLocalisation(localisation);
        if (full) {
            contentDto.setNumbersOfParts(textHolder.getNumbersOfParts());
            contentDto.setLocalMenu(textHolder.getLocalMenu());
            contentDto.setGlobalMenu(textHolder.getGlobalMenu());
            contentDto.setGlobalTexts(new HashMap<>());
            selectBeginLoadMaterials(textHolder, contentDto);
        } else {
            contentDto.setGlobalTexts(selectTextsOfLoad(textHolder, part));
        }
        return contentDto;
    }

    public void selectBeginLoadMaterials(AbstractTextHolder textHolder, ContentDto contentDto) {
        for (int i = 1; i < 5; i++) {
            contentDto.getGlobalTexts().put(i * 1000, textHolder.getGlobalTexts().get(i * 1000));
            if (textHolder.getNumbersOfParts().get(i * 1000) != null)
                for (int j = 1; j <= textHolder.getNumbersOfParts().get(i * 1000); j++) {
                    if (textHolder.getNumbersOfParts().get(i * 1000 + j * 100) > 0)
                        contentDto.getGlobalTexts().put(i * 1000 + j * 100 + 1, textHolder.getGlobalTexts().get(i * 1000 + j * 100 + 1));
                }

        }
    }

    public Map<Integer, String> selectTextsOfLoad(AbstractTextHolder textHolder, int part) {
        Map<Integer, String> globalTexts = new HashMap<>();
        int numbers;
        int chapter = part / 1000 * 1000;
        if ((part - chapter) == 0) {
            if (textHolder.getNumbersOfParts().get(chapter) != null) {
                numbers = textHolder.getNumbersOfParts().get(chapter);
                for (int i = 1; i <= numbers; i++) {
                    int loadingPart = chapter + i * 100;
                    if (textHolder.getNumbersOfParts().get(loadingPart) != null) {
                        int numbersPart = textHolder.getNumbersOfParts().get(loadingPart);
                        if (numbersPart > 1)
                            for (int j = 2; j <= numbersPart; j++) {
                                globalTexts.put((loadingPart + j), textHolder.getGlobalTexts().get(loadingPart + j));
                            }
                    }
                }
            }
        }
        return globalTexts;
    }
}
