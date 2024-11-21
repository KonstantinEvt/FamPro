package com.example.transcripters;

import com.ibm.icu.text.Transliterator;

public class RusTranscriter implements AbstractTranscriter {


    public static final String RUSSIAN_LATIN_BGN = "Russian-Latin/BGN";
    public static final String LATIN_RUSSIAN_BGN = "Latin-Russian/BGN";


    @Override
    public String transcritWordToLocalisation(String str) {

        return Transliterator.getInstance(LATIN_RUSSIAN_BGN).transliterate(str);
    }
    @Override
    public String transcritWordFromLocalisation(String str) {
        return Transliterator.getInstance(RUSSIAN_LATIN_BGN).transliterate(str);
    }


}

