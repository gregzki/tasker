package com.gzs.tasker.element;

import com.gzs.tasker.Display;
import com.gzs.tasker.DisplayImpl;
import com.gzs.tasker.state.Task;
import javafx.event.ActionEvent;

import java.time.Instant;

public class TaskButton extends CountingButton implements Display {
    private final Task task;
    private long todayValue;
    private final long nonTodayValue;

    private long startEpoch;
    private long currentRunTimerValue;

    private final DisplayImpl display;

    public TaskButton(Task task, DisplayImpl display) {
        super(task.getTitle());
        this.task = task;
        this.display = display;
        this.todayValue = task.computeTodayCount();
        this.nonTodayValue = task.computeAllCount() - todayValue;
        this.timerValue = getCountToDisplay(display.getMode());
        this.startEpoch = Instant.now().getEpochSecond();
        updateTextWithCounter();
    }

    private long getCountToDisplay(DisplayImpl.Mode displayMode) {
        return switch (displayMode) {
            case TODAY -> todayValue;
            case FULL -> nonTodayValue + todayValue;
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
        todayValue++;
        task.reportTime(startEpoch, currentRunTimerValue);
    }

    @Override
    public void refresh() {
        timerValue = getCountToDisplay(display.getMode());
        updateTextWithCounter();
    }

    @Override
    void setTitle(String title) {
        this.text = title;
        if (task != null) {
            task.setTitle(title);
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
        //TODO: fix removing task
    }

    @Override
    void addMinutes(int value) {
        super.addMinutes(value);

        long seconds = value * 60L;
        long correctionToCurrentRun = 0L;
        todayValue += seconds;
        if (todayValue < 0) {
            correctionToCurrentRun = todayValue;
            todayValue = 0L;
        }
        currentRunTimerValue += seconds - correctionToCurrentRun;
        refresh();
        task.reportTime(startEpoch, currentRunTimerValue);
    }
}
