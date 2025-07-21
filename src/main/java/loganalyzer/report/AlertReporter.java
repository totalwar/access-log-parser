package loganalyzer.report;

import java.io.IOException;
import java.time.LocalDateTime;

public interface AlertReporter {
    /**
     * Вывод сообщения пользователю
     *
     * @param fromInterval начало проблемного интервала
     * @param toInterval   конец проблемного интервала
     * @param availability рассчитанное значение доступности
     * @param errors       кол-во ошибок
     */
    void alert(LocalDateTime fromInterval, LocalDateTime toInterval, double availability) throws IOException;
}
