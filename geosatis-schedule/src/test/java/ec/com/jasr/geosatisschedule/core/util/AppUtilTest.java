package ec.com.jasr.geosatisschedule.core.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;

class AppUtilTest {

    @Test
    void shouldReturnNextMondayIfTodayIsWeekend() {
        LocalDate nextWeekDay = AppUtil.getNextWeekDate();

        DayOfWeek day = DayOfWeek.of(LocalDate.now().get(ChronoField.DAY_OF_WEEK));
        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) {
            assertEquals(DayOfWeek.MONDAY.equals(day) ? LocalDate.now() : LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)), nextWeekDay);
        } else {
            assertEquals(LocalDate.now(), nextWeekDay);
        }
    }

    @Test
    void shouldReturnNextSaturdayIfTodayIsWeekDay() {
        LocalDate nextWeekDay = AppUtil.getNextWeekendDate();

        DayOfWeek day = DayOfWeek.of(LocalDate.now().get(ChronoField.DAY_OF_WEEK));
        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) {
            assertEquals(LocalDate.now(), nextWeekDay);
        } else {
            assertEquals(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)), nextWeekDay);
        }
    }

    @Test
    void isWeekend() {
        Boolean isWeekend = AppUtil.isWeekend(LocalDate.now());

        DayOfWeek day = DayOfWeek.of(LocalDate.now().get(ChronoField.DAY_OF_WEEK));
        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) {
            assertEquals(true, isWeekend);
        } else {
            assertEquals(false, isWeekend);
        }
    }

    @Test
    void getNextDateOf() {
        DayOfWeek day = DayOfWeek.of(LocalDate.now().get(ChronoField.DAY_OF_WEEK));
        assertEquals((DayOfWeek.SUNDAY.equals(day) ? LocalDate.now() : LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY))), AppUtil.getNextDateOf(DayOfWeek.SUNDAY));
    }

    @Test
    void throwError() {
        try {
            AppUtil.throwError("Test", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AppException e) {
            assertEquals("Test", e.getMessage());
            assertEquals("Test", e.getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getCode());
        }
    }
}