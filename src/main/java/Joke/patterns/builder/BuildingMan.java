package Joke.patterns.builder;

public class BuildingMan {
    public static void main(String[] args) {
        UserFam vasya=new ManBuilder()
                .setMiddle("Петрович")
                .setLast("Иванов")
                .setName("Вася")

                .buildMan();
        System.out.println(vasya);
    }
}
