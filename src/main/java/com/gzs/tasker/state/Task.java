package com.gzs.tasker.state;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Task {
    String title;
    Map<Long, Long> epochStampedCount = new HashMap<>();

    public long computeTodayCount() {
        return computeDayCount(LocalDate.now());
    }

    public long computeDayCount(LocalDate day) {
        return getDayStream(day)
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    public long computeDaySwitchCount(LocalDate day) {
        return getDayStream(day)
                .count();
    }

    private Stream<Map.Entry<Long, Long>> getDayStream(LocalDate day) {
        return epochStampedCount.entrySet().stream()
                .filter(v -> {
                            ZoneOffset offset = ZonedDateTime.now().getOffset();
                            long startEpoch = day.atStartOfDay().toEpochSecond(offset);
                            long endEpoch = day.atTime(LocalTime.MAX).toEpochSecond(offset);
                            Long key = v.getKey();
                            return key >= startEpoch && key < endEpoch;
                        }
                );
    }

    public long computeAllCount() {
        return epochStampedCount.values().stream()
                .mapToLong(v -> v)
                .sum();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void reportTime(long startEpochSeconds, long timerValue) {
        epochStampedCount.put(startEpochSeconds, timerValue);
    }

    public void clearTimeReport() {
        epochStampedCount.clear();
    }

    public List<LocalDate> getReportedDays() {
        ZoneOffset offset = ZonedDateTime.now().getOffset();
        return epochStampedCount.keySet().stream()
                .map(epoch ->
                        LocalDateTime
                                .ofEpochSecond(epoch, 0, offset)
                                .toLocalDate())
                .distinct()
                .toList();
    }
}
