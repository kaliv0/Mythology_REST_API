package com.kaliv.myths.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Clock {
    public static final String EE_TIME_ZONE = "Europe/Sofia";

    public static ZonedDateTime getZonedDateTime() {
        Instant nowUtc = Instant.now();
        ZoneId europeSofiaId = ZoneId.of(EE_TIME_ZONE);
        return ZonedDateTime.ofInstant(nowUtc, europeSofiaId);
    }
}
