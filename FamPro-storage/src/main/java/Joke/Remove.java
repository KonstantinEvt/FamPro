package Joke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Remove {


    public static void main(String[] args) {
        int[] arr={1,2,3,4,5,6,3,21,4,5,6,7,3,3,};
        int r=3;
        int[] aa= new Random().ints(100).toArray();
        int[] arr1=remove(arr,r);
        Arrays.sort(aa);
        Arrays.stream(arr1).forEach(System.out::println);

    }
    public static int[] remove(int[] arr,int r){
        return Arrays.stream(arr).filter(x->x!= 3).toArray();

    }

}
