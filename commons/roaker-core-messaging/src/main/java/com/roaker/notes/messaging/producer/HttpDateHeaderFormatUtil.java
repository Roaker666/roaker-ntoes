package com.roaker.notes.messaging.producer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lei.rao
 * @since 1.0
 */
public class HttpDateHeaderFormatUtil {
    public static String nowAsHttpDateString() {
        return timeAsHttpDateString(ZonedDateTime.now(ZoneId.of("GMT")));
    }

    public static String timeAsHttpDateString(ZonedDateTime gmtTime) {
        return gmtTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
