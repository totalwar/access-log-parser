package loganalyzer.clf;

import java.time.LocalDateTime;

public final class DateUtils {
    private DateUtils() {
    }

    /**
     * Парсим формат [dd/MM/yyyy:HH:mm:ss +XXXX]
     * Этот метод быстрее чем LocalDateTime.parse() в 30-40 раз.
     */
    public static LocalDateTime parseDateWithBraces(String rawDate) {
        int d = toDigit(rawDate.charAt(1)) * 10 + toDigit(rawDate.charAt(2));
        int m = toDigit(rawDate.charAt(4)) * 10 + toDigit(rawDate.charAt(5));
        int y = toDigit(rawDate.charAt(7)) * 1000 + toDigit(rawDate.charAt(8)) * 100 + toDigit(rawDate.charAt(9)) * 10 + toDigit(rawDate.charAt(10));

        int hh = toDigit(rawDate.charAt(12)) * 10 + toDigit(rawDate.charAt(13));
        int mm = toDigit(rawDate.charAt(15)) * 10 + toDigit(rawDate.charAt(16));
        int ss = toDigit(rawDate.charAt(18)) * 10 + toDigit(rawDate.charAt(19));

        return LocalDateTime.of(y, m, d, hh, mm, ss);
    }

    private static int toDigit(char ch) {
        return ch - '0';
    }
}
