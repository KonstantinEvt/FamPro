package ru.memman.holders;

import ru.memman.models.SimpleText;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface AbstractTextHolder {
    Map<Integer,String> getGlobalMenu();
    Map<Integer,String> getGlobalTexts();
    Map<Integer,Integer> getNumbersOfParts();
    Map<Integer,String> getLocalMenu();

    void loadGlobalMenu();

    void loadLocalMenu();

    void loadGlobalTexts();

    default Map<Integer, String> loadTextContent(String path) {
        Map<Integer, String> loading = new ConcurrentHashMap<>();
        try (BufferedReader fr = new BufferedReader(new FileReader(path))) {
            String readLine = fr.readLine();
            ObjectMapper ss = new ObjectMapper();
            while (readLine != null) {
                SimpleText simpleText = ss.readValue(readLine, SimpleText.class);
                loading.put(simpleText.getChoice(), simpleText.getText());
                readLine = fr.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loading;
    }

    default Map<Integer, Integer> receiveNumberMatrix(Map<Integer,String> globalMenu){
        Map<Integer, Integer> numbersOfParts = new ConcurrentHashMap<>();
        for (Integer i :
                globalMenu.keySet()) {
            int part = (i / 100) * 100;
            if (!i.equals(part)) numbersOfParts.merge(part, 1, (x, y) -> x = x + 1);
            else {
                int chapter = (i / 1000) * 1000;
                if (!i.equals(chapter)) numbersOfParts.merge(chapter, 1, (x, y) -> x = x + 1);
            }
        }
        return numbersOfParts;
    }
}
