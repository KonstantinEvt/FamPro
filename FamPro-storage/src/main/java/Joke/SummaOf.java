package Joke;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class SummaOf implements Runnable{
    int[] arr;
    @Override
    public void run() {
        OperationMultyThead.map.put("Summa in ".concat(Thread.currentThread().getName()), Arrays.stream(arr).sum());

    }
}
