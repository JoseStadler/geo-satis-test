package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util;

public enum ScheduleEventWeekDays {

    EVERY_DAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7),
    WEEK_DAY(8),
    WEEKENDS(9);

    private int day;

    public int getValue() {
        return day;
    }

    private ScheduleEventWeekDays(int day) {
        this.day = day;
    }

}
