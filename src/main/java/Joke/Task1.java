package Joke;

import lombok.AllArgsConstructor;

import java.util.stream.IntStream;

@AllArgsConstructor
public class Task1 implements Runnable{
    int entry;

    @Override
    public void run() {
        MulttiThread.setMap(entry,IntStream.rangeClosed(1,entry).reduce(1,(x,y)->x*y));
       // System.out.println(entry);
    }
}
