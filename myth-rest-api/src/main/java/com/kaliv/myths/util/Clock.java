package com.kaliv.myths.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.kaliv.myths.constant.params.Args.EE_TIME_ZONE;

public class Clock {
    public static ZonedDateTime getZonedDateTime() {
        Instant nowUtc = Instant.now();
        ZoneId europeSofiaId = ZoneId.of(EE_TIME_ZONE);
        return ZonedDateTime.ofInstant(nowUtc, europeSofiaId);
    }
}
