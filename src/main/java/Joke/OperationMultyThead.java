package Joke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OperationMultyThead {
    static Map<String,Integer> map=new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        int[] arr={1,2,3,4,5,1,7,2,22};
        int[] arr2={34,24,7,54,32,8,4,4};
        ExecutorService executorService= Executors.newFixedThreadPool(2);
        executorService.submit(new SummaOf(arr));
        executorService.submit(new MultyOf(arr));
        executorService.submit(new SummaOf(arr2));
        executorService.submit(new MultyOf(arr2));
        Thread.currentThread().sleep(4000);
        map.entrySet().forEach(System.out::println);
        executorService.shutdown();
    }
}
