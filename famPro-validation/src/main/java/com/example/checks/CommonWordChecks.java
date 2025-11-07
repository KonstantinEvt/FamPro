package com.example.checks;

import com.example.transcriters.TranscriterHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class CommonWordChecks {
    public String checkForBlanks(String string) {
        return (string.isBlank()) ? null : string;
    }

    public void checkForSwears(TranscriterHolder transcriterHolder, String t) {
    }

    public String checkForMulti(TranscriterHolder transcriterHolder, String string, boolean enableMatrixOfChange) {
        List<String> result = new LinkedList<>();
        String change = "";
        String[] str = string.split("[.,\\- _()№]");
        for (String st :
                str) {
            String st1 = checkForBlanks(st);
            if (st1 == null) continue;
            checkForSwears(transcriterHolder, st1);
            if (enableMatrixOfChange) {
                st1 = setUpperFirst(st1.toLowerCase());
                if (transcriterHolder.getTranscriter().getMatrixOfChange().containsKey(st1))
                    change = checkForChange(transcriterHolder, st1);
                else result.add(st1);
            } else result.add(setUpperFirst(st1.toLowerCase()));
        }
        String res=String.join("-", result);
        if (!enableMatrixOfChange) return (res.equals("null")) ? null : res;
        else return (res.equals("null")) ? null : (change + res);
    }

    public String setUpperFirst(String string) {
        String firthChar=String.valueOf(string.charAt(0)).toUpperCase();
        if (string.length() == 1) return firthChar;
        else return firthChar + string.substring(1).toLowerCase();
    }

    public String checkForChange(TranscriterHolder transcriterHolder, String string) {
        return transcriterHolder.getTranscriter().getMatrixOfChange().getOrDefault(string, string);
    }
}
