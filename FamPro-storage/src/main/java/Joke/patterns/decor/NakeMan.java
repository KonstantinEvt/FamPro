package Joke.patterns.decor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NakeMan implements Man{
    String body;
    Integer massa;
    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Integer getMassa() {
        return null;
    }
}
