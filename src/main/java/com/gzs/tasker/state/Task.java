package com.gzs.tasker.state;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Task {
    String title;
    Map<Long, Long> epochStampedCount = new HashMap<>();

    public long computeTodayCount() {
        return epochStampedCount.entrySet().stream()
                .filter(v -> {
                            LocalDate now = LocalDate.now();
                            ZoneOffset offset = ZonedDateTime.now().getOffset();
                            long startEpoch = now.atStartOfDay().toEpochSecond(offset);
                            long endEpoch = now.atTime(LocalTime.MAX).toEpochSecond(offset);
                            Long key = v.getKey();
                            return key >= startEpoch && key < endEpoch;
                        }
                )
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    public long computeAllCount() {
        return epochStampedCount.values().stream()
                .mapToLong(v -> v)
                .sum();
    }

    public String getTitle() {
        return title;
    }

    public void reportTime(long startEpochSeconds, long timerValue) {
        epochStampedCount.put(startEpochSeconds, timerValue);
    }
}
