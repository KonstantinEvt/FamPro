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
public class TextHolderRu implements AbstractTextHolder {
    @Value("${application.text.ru}")
    private String textPath;
    @Value("${application.storageAll}")
    private String mainPath;
    private Map<Integer, String> globalMenu;
    private Map<Integer, Integer> numbersOfParts;
    private Map<Integer, String> localMenu;
    private Map<Integer, String> globalTexts;

    @PostConstruct
    @Override
    public void loadGlobalMenu() {
        try {
            globalMenu = loadTextContent(mainPath.concat(textPath.concat("/globalMenu.txt")));
            log.info("loading GlobalMenu (Russian) is done");
            numbersOfParts=receiveNumberMatrix(globalMenu);
            log.info("Numbers of Chapters: {},{},{},{}", numbersOfParts.get(1100),numbersOfParts.get(1200),numbersOfParts.get(1300),numbersOfParts.get(1400));
        } catch (RuntimeException e) {
            globalMenu = new ConcurrentHashMap<>();
            numbersOfParts=new ConcurrentHashMap<>();
            log.warn("loading GlobalMenu (Russian) not happened - map of GlobalMenu is Empty");
        }

    }

    @PostConstruct
    @Override
    public void loadLocalMenu() {
        try {
            localMenu = loadTextContent(mainPath.concat(textPath.concat("/localMenu.txt")));
            log.info("loading localeMenu (Russian) is done");
        } catch (RuntimeException e) {
            localMenu = new ConcurrentHashMap<>();
            log.warn("loading localeMenu (Russian) not happened - map of localeMenu is Empty");
        }
    }

    @PostConstruct
    @Override
    public void loadGlobalTexts() {
        try {
            globalTexts = loadTextContent(mainPath.concat(textPath.concat("/globalTexts.txt")));
            log.info("loading globalTexts (Russian) is done");
        } catch (RuntimeException e) {
            globalTexts = new ConcurrentHashMap<>();
            log.warn("loading globalTexts (Russian) not happened - map of globalTexts is Empty");
        }
    }

}