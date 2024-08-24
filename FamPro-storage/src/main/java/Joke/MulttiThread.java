package Joke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MulttiThread {
    private static final Map<Integer,Integer> mapFactorial =new ConcurrentHashMap<>(100,0.75f,5);
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService= Executors.newFixedThreadPool(2);
        for (int i = 1; i < 10; i++) {
            executorService.submit(new Task1(i));
        }
        String name=Thread.currentThread().getName();
        Thread.sleep(1);
        executorService.shutdown();
        mapFactorial.entrySet().forEach(System.out::println);
       }
    public static void setMap(Integer i,Integer j) {
        mapFactorial.put(i,j);
    }
}
