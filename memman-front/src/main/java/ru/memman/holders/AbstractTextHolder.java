package ru.memman.holders;

import ru.memman.models.SimpleStringText;
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
    Map<String,String> getLangMatrix();
    Map<Integer,String> getLocalMenu();

    void loadGlobalMenu();

    void loadLocalMenu();

    void loadGlobalTexts();

    void loadLangMatrix();
    default Map<Integer, String> loadNumberedContent(String path) {
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

    default Map<String, String> loadStringContent(String path){
        Map<String, String> loading = new ConcurrentHashMap<>();
        try (BufferedReader fr = new BufferedReader(new FileReader(path))) {
            String readLine = fr.readLine();
            ObjectMapper ss = new ObjectMapper();
            while (readLine != null) {
                SimpleStringText simpleText = ss.readValue(readLine, SimpleStringText.class);
                loading.put(simpleText.getChoice(), simpleText.getText());
                readLine = fr.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loading;
    }
}
