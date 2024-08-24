package Joke.patterns.fabricMethod;

public class Fact {
    public static void main(String[] args) {
        FactoryOfTransport factoryOfTransport=new FactoryOfTransport();
        Transport transport=factoryOfTransport.getTransport("car");
        transport.move();
    }
}
