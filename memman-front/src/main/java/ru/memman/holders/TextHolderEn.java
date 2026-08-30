package ru.memman.holders;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Log4j2
public class TextHolderEn implements AbstractTextHolder {
    @Value("${application.text.en}")
    private String textPath;
    @Value("${application.storageAll}")
    private String mainPath;
    private Map<Integer, String> globalMenu;
    private Map<String, String> langMatrix;
    private Map<Integer, String> localMenu;
    private Map<Integer, String> globalTexts;

    @PostConstruct
    @Override
    public void loadGlobalMenu() {
        try {
            globalMenu = loadNumberedContent(mainPath.concat(textPath.concat("/globalMenu.txt")));
            log.info("loading globalMenu (English) is done");
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            globalMenu=new ConcurrentHashMap<>();
            langMatrix =new ConcurrentHashMap<>();
            log.warn("loading globalMenu (English) not happened - map of globalMenu is Empty");
        }
    }

    @PostConstruct
    @Override
    public void loadLocalMenu() {
        try {
            localMenu = loadNumberedContent(mainPath.concat(textPath.concat("/localMenu.txt")));
            log.info("loading localeMenu (English) is done");
        } catch (RuntimeException e) {
            localMenu=new ConcurrentHashMap<>();
            log.warn("loading localeMenu (English) not happened - map of localeMenu is Empty");
        }
    }

    @PostConstruct
    @Override
    public void loadGlobalTexts() {
        try {
            globalTexts = loadNumberedContent(mainPath.concat(textPath.concat("/globalTexts.txt")));
            log.info("loading globalTexts (English) is done");
        } catch (RuntimeException e) {
            globalTexts=new ConcurrentHashMap<>();
            log.warn("loading globalTexts (English) not happened - map of globalTexts is Empty");
        }
    }
    @PostConstruct
    @Override
    public void loadLangMatrix() {
        try {
            langMatrix = loadStringContent(mainPath.concat(textPath.concat("/langMatrix.txt")));
            log.info("loading langMatrix (English) is done");
        } catch (RuntimeException e) {
            globalTexts=new ConcurrentHashMap<>();
            log.warn(e.getMessage());
            log.warn("loading langMatrix (English) not happened - map of langMatrix is Empty");
        }
    }
}