package com.example.translators;

import com.ibm.icu.text.Transliterator;

public class Transcrit {


    public static final String CYRILLIC_TO_LATIN = "Cyrillic-Latin";
    public static final String LATIN_TO_CYRILLIC = "Latin-Cyrillic";

    public static void main(String[] args) {
        String st = "привет мир";

        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        Transliterator toCyrillicTrans = Transliterator.getInstance(LATIN_TO_CYRILLIC);
        String result = toLatinTrans.transliterate(st);


    }
}

