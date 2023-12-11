package com.gzs.tasker.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    Task task;

    ZoneOffset offset = ZonedDateTime.now().getOffset();

    @BeforeEach
    void setUp() {
        task = new Task("test");

        task.reportTime(LocalDateTime.parse("2023-11-30T10:00:00").toEpochSecond(offset), LocalTime.parse("00:00:10").toSecondOfDay());

        task.reportTime(LocalDateTime.parse("2023-11-30T23:00:00").toEpochSecond(offset), LocalTime.parse("00:10:00").toSecondOfDay());

        task.reportTime(LocalDateTime.parse("2023-12-01T10:00:00").toEpochSecond(offset), LocalTime.parse("01:00:00").toSecondOfDay());

        task.reportTime(LocalDateTime.parse("2023-12-15T10:00:00").toEpochSecond(offset), LocalTime.parse("00:30:00").toSecondOfDay());
        task.reportTime(LocalDateTime.parse("2023-12-15T10:30:01").toEpochSecond(offset), LocalTime.parse("00:15:00").toSecondOfDay());
        task.reportTime(LocalDateTime.parse("2023-12-15T22:55:00").toEpochSecond(offset), LocalTime.parse("00:15:00").toSecondOfDay());

        task.reportTime(LocalDateTime.parse("2023-12-31T10:15:01").toEpochSecond(offset), LocalTime.parse("00:15:00").toSecondOfDay());
    }

    @Test
    void computeDayCount() {
        assertThat(
                task.computeDayCount(LocalDate.parse("2023-12-15")))
                .isEqualTo(
                        LocalTime.parse("01:00:00").toSecondOfDay()
                );

        assertThat(
                task.computeDayCount(LocalDate.parse("2023-12-01")))
                .isEqualTo(
                        LocalTime.parse("01:00:00").toSecondOfDay()
                );

        assertThat(
                task.computeDayCount(LocalDate.parse("2023-12-31")))
                .isEqualTo(
                        LocalTime.parse("00:15:00").toSecondOfDay()
                );
    }

    @Test
    void computeMonthCount() {
        assertThat(
                task.computeMonthCount(YearMonth.parse("2023-11")))
                .isEqualTo(
                        LocalTime.parse("00:10:10").toSecondOfDay()
                );

        assertThat(
                task.computeMonthCount(YearMonth.parse("2023-12")))
                .isEqualTo(
                        LocalTime.parse("02:15:00").toSecondOfDay()
                );
    }

    @Test
    void getReportedDays() {
        assertThat(
                task.getReportedDays())
                .containsExactlyElementsOf(
                        List.of(
                                LocalDate.parse("2023-11-30"),
                                LocalDate.parse("2023-12-01"),
                                LocalDate.parse("2023-12-15"),
                                LocalDate.parse("2023-12-31")
                        )
                );
    }
}