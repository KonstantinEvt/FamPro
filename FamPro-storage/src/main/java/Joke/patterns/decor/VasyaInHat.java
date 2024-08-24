package Joke.patterns.decor;

public class VasyaInHat implements Man{
    private Man man;
    VasyaInHat(Man man){this.man=man;}
    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Integer getMassa() {
        return null;
    }
}
