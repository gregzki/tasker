package com.gzs.tasker.state;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Task {
    String title;
    Map<Long, Long> epochStampedCount = new HashMap<>();

    public long computeTodayCount() {
        return epochStampedCount.values().stream()
                .filter(v -> {
                            LocalDate now = LocalDate.now();
                            long startEpoch = now.atStartOfDay().toEpochSecond(null);
                            long endEpoch = now.atTime(LocalTime.MAX).toEpochSecond(null);
                            return v >= startEpoch && v < endEpoch;
                        }
                )
                .mapToLong(v -> v)
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

    public void reportTime(long startEpoch, long timerValue) {
        epochStampedCount.put(startEpoch, timerValue);
    }
}
