package com.gzs.tasker.util;

import java.time.ZonedDateTime;

public class CurrentZone {
    private static final java.time.ZoneOffset zoneOffset = ZonedDateTime.now().getOffset();

    private CurrentZone() {
    }

    public static java.time.ZoneOffset offset() {
        return zoneOffset;
    }
}
