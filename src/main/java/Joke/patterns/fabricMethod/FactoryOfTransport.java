package Joke.patterns.fabricMethod;

public class FactoryOfTransport {
    public Transport getTransport(String str){
        if (str.equals("car"))return new Car();
        return new Ship();
    };
}
