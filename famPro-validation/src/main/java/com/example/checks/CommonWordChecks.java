package com.example.checks;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommonWordChecks {
    public String checkForBlanks(String string){
        if (string.isBlank()) return null;
        return string.replaceAll(" ","");
    }
    public void checkForSwears(String t){
    }
}
