package Joke.patterns.builder;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserFam {
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDateTime birthday;
}
