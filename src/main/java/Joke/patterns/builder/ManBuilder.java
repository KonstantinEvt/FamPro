package Joke.patterns.builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManBuilder implements AbstractBuilder{
private UserFam userFam;
    ManBuilder(){
    userFam=new UserFam();
}
    @Override
    public UserFam buildMan() {
        return userFam;
    }
    public ManBuilder setName(String name){
        userFam.setName(name);
        return this;
    }
    public ManBuilder setMiddle(String middleName){
        userFam.setMiddleName(middleName);
        return this;
    }
    public ManBuilder setLast(String lastName){
        userFam.setLastName(lastName);
           return this;
    }
    public ManBuilder setBirthday(String birthday){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy MM dd");
        userFam.setBirthday(LocalDateTime.parse(birthday,formatter));
        return this;
    }
}
