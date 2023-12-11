package com.gzs.tasker.element;

import com.gzs.tasker.Display;
import com.gzs.tasker.TasksDisplayHandler;
import com.gzs.tasker.state.Task;
import javafx.event.ActionEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;

public class TaskButton extends CountingButton implements Display {
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
        updateDayValues();
        this.display = tasksDisplayHandler;
        this.timerValue = getCountToDisplay(tasksDisplayHandler.getMode());
        this.startEpoch = Instant.now().getEpochSecond();
        updateTextWithCounter();
    }

    private void updateDayValues() {
        if (dayValueDate == null || !LocalDate.now().isEqual(dayValueDate)) {
            dayValueDate = LocalDate.now();
            this.dayValue = task.computeDayCount(dayValueDate);
            this.otherDaysOfMonthValue = task.computeMonthCount(YearMonth.from(dayValueDate)) - dayValue;
        }
    }

    private long getCountToDisplay(TasksDisplayHandler.Mode displayMode) {
        updateDayValues();
        return switch (displayMode) {
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
        timerValue = getCountToDisplay(display.getMode());
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

    public void archiveTask() {
        task.archive();
    }
}
