package Joke;

import java.util.Arrays;
import java.util.Random;

public class RandomNumber {
    public static void main(String[] args) {
        int[] arr = new int[10];
        for (int j = 0; j < 10; j++) {
            arr[j] = new Random().nextInt();
        }
        System.out.format("max=%d%n", Arrays.stream(arr).min().orElse(0));
        System.out.format("max=%d%n", Arrays.stream(arr).max().orElse(0));
        System.out.format("max=%.2f%n", Arrays.stream(arr).average().orElse(0.0d));
    }

    static int[] getSort(int[] arr) {
        return Arrays.stream(arr).sorted().toArray();
    }

}
