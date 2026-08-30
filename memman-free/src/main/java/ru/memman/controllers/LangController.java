package ru.memman.controllers;

import ru.memman.models.ContentDto;
import ru.memman.service.TextSelector;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/free/languish")
@AllArgsConstructor

public class LangController {

    private TextSelector textSelector;

    @PostMapping(value = "/beginLoad/{currentLang}")
    public ContentDto beginLoader(@PathVariable("currentLang") String currentLang) {
        return textSelector.getContentDto(currentLang,0,true);
    }
    @PostMapping(value = "/parts/{currentLang}/{part}")
    public ContentDto getMenu(@PathVariable("currentLang") String currentLang, @PathVariable("part") int part) {
        return textSelector.getContentDto(currentLang,part,false);
    }


}


