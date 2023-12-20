package com.gzs.tasker.element;

import com.gzs.tasker.Display;
import com.gzs.tasker.TasksDisplayHandler;
import com.gzs.tasker.state.Task;
import com.gzs.tasker.util.CurrentZone;
import javafx.event.ActionEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskButton extends CountingButton implements Display {
    private static final Logger LOGGER = Logger.getLogger(TaskButton.class.getName());
    private final Task task;
    private LocalDate dayValueDate;
    private long dayValue;
    private long otherDaysOfMonthValue;

    private long startEpoch;
    private long currentRunTimerValue;

    private final TasksDisplayHandler display;

    public TaskButton(Task task, TasksDisplayHandler tasksDisplayHandler) {
        super(task.getTitle());
        this.task = task;
        display = tasksDisplayHandler;
        timerValue = getCountToDisplay();
        startEpoch = Instant.now().getEpochSecond();
        updateTextWithCounter();
    }

    private void updateDayValues() {
        LOGGER.log(Level.FINE, "Update values {0} {1} {2}", new Object[]{dayValueDate, dayValue, otherDaysOfMonthValue});
        if (dayValueDate == null || !LocalDate.now().isEqual(dayValueDate)) {
            dayValueDate = LocalDate.now();
            LOGGER.log(Level.FINE, "Updated to {0} {1} {2}", new Object[]{dayValueDate, dayValue, otherDaysOfMonthValue});
            dayValue = task.computeDayCount(dayValueDate);
            otherDaysOfMonthValue = task.computeMonthCount(YearMonth.from(dayValueDate)) - dayValue;
            currentRunTimerValue = 0L;
        }
    }

    private long getCountToDisplay() {
        updateDayValues();
        return switch (display.getMode()) {
            case TODAY -> dayValue;
            case MONTH -> otherDaysOfMonthValue + dayValue;
            case LAST_RUN -> currentRunTimerValue;
        };
    }

    @Override
    public void play() {
        startEpoch = Instant.now().getEpochSecond();
        currentRunTimerValue = 0;
        super.play();
    }

    @Override
    void timerTickHandler(ActionEvent ev) {
        super.timerTickHandler(ev);
        currentRunTimerValue++;
        dayValue++;
        task.reportTime(startEpoch, currentRunTimerValue);
    }

    @Override
    public void refresh() {
        timerValue = getCountToDisplay();
        updateTextWithCounter();
    }

    @Override
    public void setTitle(String title) {
        this.text = title;
        if (task != null) {
            task.setTitle(title);
            display.sortTasksButtons();
        }
    }

    @Override
    void resetCounter() {
        super.resetCounter();
        if (task != null) {
            task.clearTimeReport();
        }
    }

    @Override
    void removeThisButton() {
        super.removeThisButton();
        display.removeTask(this);
    }

    @Override
    void addMinutes(int value) {
        checkDayChangeUpdate();
        super.addMinutes(value);

        long seconds = value * 60L;
        long correctionToCurrentRun = 0L;
        dayValue += seconds;
        if (dayValue < 0) {
            correctionToCurrentRun = dayValue;
            dayValue = 0L;
        }
        currentRunTimerValue += seconds - correctionToCurrentRun;
        refresh();

        task.reportTime(startEpoch, currentRunTimerValue);
    }

    private void checkDayChangeUpdate() {
        LocalDate startDate = LocalDateTime.ofEpochSecond(startEpoch, 0, CurrentZone.offset())
                .toLocalDate();
        if (startDate.isBefore(LocalDate.now())) {
            startEpoch = Instant.now().getEpochSecond();
        }
    }

    public void archiveTask() {
        task.archive();
    }
}
