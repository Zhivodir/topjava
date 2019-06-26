package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return (startTime != null ? lt.compareTo(startTime) >= 0 : true) &&
                (endTime != null ? lt.compareTo(endTime) <= 0 : true);
    }

    public static boolean isBetween(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return (startDate != null ? ld.compareTo(startDate) >= 0 : true) &&
                (endDate != null ? ld.compareTo(endDate) <= 0 : true);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate convertToLocalDate(String datetime) {
        return datetime.isEmpty() ? null : LocalDate.parse(datetime);
    }

    public static LocalTime convertToLocalTime(String datetime) {
        return datetime.isEmpty() ? null : LocalTime.parse(datetime);
    }
}
