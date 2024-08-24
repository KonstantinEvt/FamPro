package Joke;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class Task implements Callable<Integer> {
    Integer x,y,j;
    public Task(Integer i, Integer y,Integer j) {
        this.x = i;
        this.y = y;
        this.j=j;

    }
;
    @Override
    public Integer call() throws InterruptedException {
    sleep(j);
        return x+y;
     }
}
