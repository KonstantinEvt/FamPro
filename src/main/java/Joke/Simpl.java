package Joke;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Simpl {
    public static void main(String[] args) {
        List<Integer> aa = new ArrayList<>();
        int size = 100;
        aa.add(2);
        int j;
        for (int i = 2; i < size; i++) {
            for (j = 0; j < aa.size(); j++)
                if (i % aa.get(j) == 0) {
                    break;
                }
            if (j==aa.size()) aa.add(i);
        }
        aa.forEach(x -> System.out.format("%d ", x));
    }
}

