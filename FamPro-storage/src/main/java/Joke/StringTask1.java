package Joke;

import org.springframework.asm.Type;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringTask1 {
    public static void main(String[] args) {
        String[] strings = {"ss", "aadsd", "sdafgf", "sfseesfs"};
        String str = findMaxLengthString(strings);
        String st1 = "d ss dd ss dfgh ss ds sss ss df dd ss fd df sd df fg df e ds fg sd as   df sd ss ddf";
        getMapOfWord(st1).entrySet().forEach(System.out::println);

        System.out.println(checkEntry(st1, "ss"));
        System.out.println(isPalendron(str));
        System.out.println(st1);
    }

    public static String findMaxLengthString(String[] strings) {
        int j = strings[0].length();
        int k = 0;
        for (int i = 1; i < strings.length; i++) {
            if (strings[i].length() > j) j = strings[i].length();
            k = i;
        }
        return strings[k];
    }

    public static boolean isPalendron(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);

        if (stringBuilder.reverse().toString().equals(str)) return true;
        return false;
    }

    public static String deleteWrongWords(String str) {
        return Arrays.stream(str.split(" "))
                .map(x -> {
                    if (x.equals("ss")) return "foo";
                    else return x;
                })
                .collect(Collectors.joining(" "));
    }

    public static int checkEntry(String str, String sub) {
        String[] strings = str.concat(" ").split(sub);
        Arrays.stream(strings).forEach(System.out::println);
        return strings.length - 1;
    }

    public static Map<String, Integer> getMapOfWord(String str) {
        Map<String, Integer> map = new HashMap<>();
        String[] strings = str.split(" ");
        for (String st : strings) {
            map.merge(st, 1, (x, y) -> x + 1);
        }
        Integer hh=map.values().stream().max(Integer::compareTo).orElse(0);
        for (Map.Entry<String,Integer> j: map.entrySet()) {
            if (map.get(j.getKey()).equals(hh)){
                System.out.println(j);
                System.out.println();
            };
        }
        return map;
    }

}

