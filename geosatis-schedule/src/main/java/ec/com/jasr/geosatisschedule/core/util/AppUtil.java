package ec.com.jasr.geosatisschedule.core.util;

import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

public class AppUtil {

    public static LocalDate getNextWeekDate() {
        LocalDate today = LocalDate.now();
        if (AppUtil.isWeekend(today)) {
            return AppUtil.getNextDateOf(DayOfWeek.MONDAY);
        }

        return today;
    }

    public static LocalDate getNextWeekendDate() {
        LocalDate today = LocalDate.now();
        if (AppUtil.isWeekend(today)) {
            return today;
        }

        return AppUtil.getNextDateOf(DayOfWeek.SATURDAY);
    }

    public static boolean isWeekend(LocalDate today) {
        DayOfWeek day = DayOfWeek.of(today.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }

    public static LocalDate getNextDateOf(DayOfWeek dayOfWeek) {
        DayOfWeek day = DayOfWeek.of(LocalDate.now().get(ChronoField.DAY_OF_WEEK));
        return dayOfWeek.equals(day) ? LocalDate.now() : LocalDate.now().with(TemporalAdjusters.next(dayOfWeek));
    }

    public static void throwError(String message, HttpStatus code) throws AppException {
        throw new AppException(message, code);
    }
}
