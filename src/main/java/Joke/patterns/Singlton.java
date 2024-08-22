package Joke.patterns;

public class Singlton {
    private static Singlton ss=null;
    private Singlton(){};


    public static Singlton getSs() {
        if (Singlton.ss==null) {
            return new Singlton();
        }
        return ss;
    }
}
