package com.example.converter;

import com.ibm.icu.text.Transliterator;
import org.springframework.stereotype.Component;

@Component
public class NameConverter {
    public String covertName(String str) {
        return Transliterator.getInstance("Russian-Latin/BGN").transliterate(str);

    }

}
