package com.gzs.tasker.state;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Task {
    private String title;
    private final Map<Long, Long> epochStampedCount = new HashMap<>();

    private boolean isArchived = false;

    public Task(String title) {
        this.title = title;
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

    public long computeMonthCount(YearMonth month) {
        return epochStampedCount.entrySet().stream()
                .filter(v -> {
                            ZoneOffset offset = ZonedDateTime.now().getOffset();
                            long startEpoch = month.atDay(1).atStartOfDay().toEpochSecond(offset);
                            long endEpoch = month.atEndOfMonth().atTime(LocalTime.MAX).toEpochSecond(offset);
                            Long key = v.getKey();
                            return key >= startEpoch && key < endEpoch;
                        }
                )
                .mapToLong(Map.Entry::getValue)
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

    public boolean isArchived() {
        return isArchived;
    }

    public void archive() {
        isArchived = true;
    }
}
