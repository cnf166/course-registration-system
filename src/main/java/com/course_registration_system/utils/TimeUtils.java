package com.course_registration_system.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class TimeUtils {
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .withZone(ZoneOffset.UTC);

    private TimeUtils() {}

    public static String toDbString(Instant instant) {
        return instant == null ? null : TIME_FORMAT.format(instant);
    }

    public static Instant fromDbString(String value) {
        return value == null ? null : Instant.parse(value);
    }
}
