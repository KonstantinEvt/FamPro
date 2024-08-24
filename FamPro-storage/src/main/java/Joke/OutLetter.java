package Joke;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OutLetter {
    public static LocalDate getDateOfSending() {
        LocalDateTime dateOfSending = LocalDateTime.now();

        // Проверка на время
        if (dateOfSending.getHour() > 18) dateOfSending = dateOfSending.plusDays(1);
        do {
            int dayOfPossibleSending = dateOfSending.getDayOfMonth();

            // Определение дня возможной отправки, согласно 1,10,20 числу
            if (dayOfPossibleSending > 1 && dayOfPossibleSending < 10) {
                dateOfSending = LocalDateTime.of(dateOfSending.getYear(),
                        dateOfSending.getMonth(), 10, 18, 0);
            } else if (dayOfPossibleSending > 10 && dayOfPossibleSending < 20) {
                dateOfSending = LocalDateTime.of(dateOfSending.getYear(),
                        dateOfSending.getMonth(), 20, 18, 0);
            } else if (dayOfPossibleSending > 20) {
                dateOfSending = dateOfSending.plusDays(15);
                dateOfSending = LocalDateTime.of(dateOfSending.getYear(),
                        dateOfSending.getMonth(), 1, 18, 0);
            }

            // Блок смещения дня отправки в зависимости от выходных дней.
            // Если недо добавить смещение от праздников - это добавить сюда.
            if (dateOfSending.getDayOfWeek() == DayOfWeek.SUNDAY)
                dateOfSending = dateOfSending.plusDays(-2);
            if (dateOfSending.getDayOfWeek() == DayOfWeek.SATURDAY)
                dateOfSending = dateOfSending.plusDays(-1);

            // Проверка на дату после смещения от выходных.
            // Если мы улетели в прошлое - смещаемся на несколько дней вперед
            // и снова проходим с do.
            if (dateOfSending.isBefore(LocalDateTime.now())) {
                dateOfSending = dateOfSending.plusDays(3);
            } else return dateOfSending.toLocalDate();
        }
        while (true);
    }

}
