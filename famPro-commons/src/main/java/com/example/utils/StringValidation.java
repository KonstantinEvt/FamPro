package com.example.utils;

import com.ibm.icu.text.Transliterator;

import java.util.*;

public class StringValidation {
    public static final String RUSSIAN_LATIN_BGN = "Russian-Latin/BGN";
    public static final String LATIN_RUSSIAN_BGN = "Latin-Russian/BGN";
    public static final String BULG_LATIN_BGN = "Bulgarian-Latin/BGN";
    public static final String KIRG_LATIN_BGN = "Kirghiz-Latin/BGN";
    public static final String BG_LATIN_BGN = "bg-bg_Latn/BGN";
    public static final String KY_LATIN_BGN = "ky-ky_Latn/BGN";

    public static void main(String[] args) {
        String str = "Я хочу. перевод: очень-очень хочу . Евтушенко Константин Сергеевич";
        String str2 = "YA khochu. perevod: ochenʹ-ochenʹ khochu . Yevtushenko Konstantin Sergeevich";
        Enumeration<String> availableIDs = Transliterator.getAvailableIDs();
        availableIDs.asIterator().forEachRemaining(id -> {
            Transliterator toLatinTrans = Transliterator.getInstance(id);
            String result = toLatinTrans.transliterate(str2);
            System.out.println(id + " : " +str2 + " -> " + result);
        });
//        String res = checkAllInstance(str2, "ru", true);
        String res2 = checkAllInstance(str2, "en", true);
        System.out.println(res2);
        Transliterator toCyrillicTrans = Transliterator.getInstance(LATIN_RUSSIAN_BGN );
        String result = toCyrillicTrans.transliterate(str2);
        System.out.println(result);
    }

    public static String checkForSwears(String str) {
        return str;
    }

    public static String trancrit(String str, Transliterator localisation, boolean guaranteed) {
        return null;

    }

    public static String checkAllInstance(String str, String localisation, boolean guaranteed) {
        List<String> stringList = new ArrayList<>();
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        char current = str.charAt(0);
        //parse entering string
        int countDel = -1, countLet = -1, beg = 0;
        if (current > 31 && current < 65) countDel = 0;
        else countLet = 0;
        for (int i = 1; i < str.length(); i++) {
            current = str.charAt(i);
            if (current > 31 && current < 65) countDel = i;
            else countLet = i;
            if (countDel - countLet == 1) {
                stringList.add(str.substring(beg, i));
                beg = i;
            } else if (countLet - countDel == 1) {
                arrayDeque.add(str.substring(beg, i));
                beg = i;
            }
        }
        if (countLet > countDel) stringList.add(str.substring(beg));
        else arrayDeque.add(str.substring(beg));


        // setUp transliterator localisation
        Optional<Transliterator> toUnivers = Optional.empty();
        if (localisation.equals("ru")) {
            toUnivers = Optional.of(Transliterator.getInstance(KY_LATIN_BGN));
        }
        if (localisation.equals("en")) {
            toUnivers = Optional.of(Transliterator.getInstance(LATIN_RUSSIAN_BGN));
        }
        // collect string with Filters
        StringBuffer sb = new StringBuffer();
        for (String checkWord : stringList) {
            String lang = checkForLang(checkWord);
            if (toUnivers.isPresent() && guaranteed ) {
                checkWord = toUnivers.get().transform(checkWord);
            }
            sb.append(checkWord);
            if (!arrayDeque.isEmpty()) sb.append(arrayDeque.poll());
        }
        return sb.toString();
    }

    public static String checkForLang(String str) {
        for (int i : str.chars().toArray())
            if (i >= 192) return "other";
            else if ((i < 90 && i > 75) || (i < 122 && i > 97))
                return "en";
        return "unknown";
    }
}
