package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util;

public enum ScheduledEventIntervalType {

    DAY("day"),
    WEEK("week"),
    MONTH("months"),
    YEAR("year");

    private String type;

    public String getStatus() {
        return type;
    }

    private ScheduledEventIntervalType(String type) {
        this.type = type;
    }
}
