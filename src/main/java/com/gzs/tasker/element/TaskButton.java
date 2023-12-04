package com.gzs.tasker.element;

import com.gzs.tasker.DisplayMode;
import com.gzs.tasker.state.Task;
import javafx.event.ActionEvent;

import java.time.Instant;

public class TaskButton extends CountingButton {
    private final DisplayMode displayMode;
    private final Task task;
    private long todayValue;
    private final long nonTodayValue;

    private long startEpoch;
    private long currentRunTimerValue;

    public TaskButton(Task task, DisplayMode displayMode) {
        super(task.getTitle());
        this.displayMode = displayMode;
        this.task = task;
        this.todayValue = task.computeTodayCount();
        this.nonTodayValue = task.computeAllCount() - todayValue;
        this.timerValue = getCountToDisplay();
        updateTextWithCounter();
    }

    private long getCountToDisplay() {
        return switch (displayMode.getValue()) {
            case TODAY -> todayValue;
            case FULL -> nonTodayValue + todayValue;
            case RECENT -> currentRunTimerValue;
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
        timerValue = getCountToDisplay();
        super.timerTickHandler(ev);
        currentRunTimerValue++;
        todayValue++;
        task.reportTime(startEpoch, currentRunTimerValue);
    }
}
