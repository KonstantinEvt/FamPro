package Joke;

import java.math.BigDecimal;

public class Covert {
    public String covertNumberToString(BigDecimal inputNumber) {

        // Проверка на неправильные входные данные
        if (inputNumber == null) return "Сумма отсутствует";
        if ((inputNumber.compareTo(BigDecimal.valueOf(100000)) > 0) ||
                inputNumber.compareTo(BigDecimal.ZERO) < 0)
            return "Сумма выходит за пределы указанного диапазона";

        //Разделение числа на составляющие
        int intPart = inputNumber.abs().intValue();
        int fractPart = inputNumber
                .remainder(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
        int decades = intPart % 100;
        int hundreds = intPart / 100 % 10;
        int thousands = intPart / 1000;
        StringBuilder rezult = new StringBuilder();

        // Конвертация тысяч
        if (thousands > 0) {
            rezult
                    .append(getDecades(thousands, 1))
                    .append(" тысяч");
            String thousandsFinal = switch (thousands % 10) {
                case 1 -> "a";
                case 2, 3, 4 -> "и";
                default -> "";
            };
            if (thousands / 10 != 1) rezult.append(thousandsFinal);
        }

        // Конвертация сотен
        if (hundreds != 0) rezult.append(getHundreds(hundreds));

        // Конвертация десятков
        if (decades != 0) {
            rezult.append(getDecades(decades, 2)).append(" рубл");
            String rublFinal = switch (decades % 10) {
                case 1 -> "ь";
                case 2, 3, 4 -> "я";
                default -> "ей";
            };
            if (decades / 10 != 1) rezult.append(rublFinal);
            else rezult.append("ей");
        } else rezult.append(" рублей");

        // Конвертация копеек
        if (fractPart != 0) {
            rezult.append(getDecades(fractPart, 1)).append(" копе");
            String groshFinal = switch (fractPart % 10) {
                case 1 -> "йка";
                case 2, 3, 4 -> "йки";
                default -> "ек";
            };
            if (fractPart / 10 != 1) rezult.append(groshFinal);
            else rezult.append("ек");
        }
        return String.valueOf(rezult.deleteCharAt(0));
    }

    /**
     * Метод обработки двузначного числа
     */
    public String getDecades(int decades, int sklon) {
        String desST = switch (decades / 10) {
            case 2 -> " двадцать";
            case 3 -> " тридцать";
            case 4 -> " сорок";
            case 5 -> " пятьдесят";
            case 6 -> " шестьдесят";
            case 7 -> " семьдесят";
            case 8 -> " восемьдесят";
            case 9 -> " девяносто";
            default -> "";
        };
        if (decades == 10) {
            desST = " десять";
        } else if (decades / 10 == 1) {
            desST = getUnits(decades % 10, 0) + "надцать";
        } else desST = desST + getUnits(decades % 10, sklon);
        return desST;
    }

    /**
     * Метод обработки однозначного числа
     */
    public String getUnits(int units, int sklon) {
        String ed = switch (units) {
            case 1 -> " один";
            case 2 -> " две";
            case 3 -> " три";
            case 4 -> " четыр";
            case 5 -> " пят";
            case 6 -> " шест";
            case 7 -> " сем";
            case 8 -> " восем";
            case 9 -> " девят";
            default -> "";
        };
        if (sklon == 1 && ed.equals(" один")) return " одна";
        if (sklon == 2 && ed.equals(" две")) return " два";
        if (sklon == 0
                || ed.equals(" один")
                || ed.equals(" две")) return ed;
        if (ed.equals(" четыр")) return " четыре";
        return ed + "ь";
    }

    /**
     * Метод обработки сотен
     */
    public String getHundreds(int hundreds) {
        return switch (hundreds) {
            case 1 -> " сто";
            case 2 -> " двести";
            case 3 -> " триста";
            case 4 -> " четыреста";
            case 5 -> " пятьсот";
            case 6 -> " шестьсот";
            case 7 -> " семьсот";
            case 8 -> " восемьсот";
            case 9 -> " девятьсот";
            default -> "";
        };
    }
}
