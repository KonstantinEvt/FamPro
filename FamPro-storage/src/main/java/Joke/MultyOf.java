package Joke;

import lombok.AllArgsConstructor;

import java.time.*;
import java.util.Arrays;

@AllArgsConstructor
public class MultyOf implements Runnable{
    int[] arr;
    LocalDateTime time=LocalDateTime.now();

    MultyOf(int[] arr){this.arr=arr;}
    @Override
    public void run() {
        Integer toMap=Arrays.stream(arr).reduce(1,(x, y)->x*y);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Long i= Duration.between(time, LocalDateTime.now()).getSeconds();
       OperationMultyThead.map.put(("Multy in "
               .concat(Thread.currentThread()
                       .getName())
               .concat("  ")
               .concat(String.valueOf(i))
               .concat("  ")), toMap) ;

    }
}
