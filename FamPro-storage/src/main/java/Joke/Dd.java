package Joke;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dd {
        // Найти первый не повторяющийся элемент
        public static void main(String[] args) {
            int[] arr = {9, 4, 9, 6, 7, 4, 5,4,7,8,8};
            int n = getFirstUnique(arr);
            System.out.println(n);
        }


        static int getFirstUnique(int[] arr) {
            Map<Integer, Integer> map = new LinkedHashMap<>();
            for (int j : arr) {
                map.merge(j, 1, (x, y) -> x + 1);
            }
            List<Integer> list;
            list=map.keySet().stream().toList();
            list.forEach(System.out::println);
            for (Integer integer : map.keySet()) {
                if (map.get(integer)==1){return integer;} ;
            }
            return 0;
    }
}
