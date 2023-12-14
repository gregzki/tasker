package com.gzs.tasker;

import com.gzs.tasker.element.TaskButton;
import com.gzs.tasker.state.Task;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TasksDisplayHandler {

    private final VBox tasksBox;

    private final ToggleGroup tasksGroup = new ToggleGroup();

    private final Set<Display> displays = new HashSet<>();
    private Mode mode = Mode.TODAY;

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TasksDisplayHandler() {
        this.tasksBox = new VBox();

        initDayChangeRefresh();
    }

    private void initDayChangeRefresh() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay();
        // it's enough to schedule refresh to be between 0-1 in the night.
        long initialDelay = Duration.between(now, nextMidnight).toHours() + 1;

        scheduler.scheduleAtFixedRate(() ->
                        displays.forEach(Display::refresh),
                initialDelay,
                TimeUnit.DAYS.toHours(1),
                TimeUnit.HOURS);
    }

    public void stopDayChangeRefresh() {
        scheduler.shutdown();
    }

    void createTaskButton(Task task) {
        ToggleButton button = new TaskButton(task, this);
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        button.setToggleGroup(tasksGroup);
        registerDisplay((Display) button);
        tasksBox.getChildren().add(button);
        sortTasksButtons();
    }

    public void removeTask(TaskButton taskButton) {
        taskButton.archiveTask();
        tasksBox.getChildren().remove(taskButton);
    }

    public void sortTasksButtons() {
        List<Node> sorted = tasksBox.getChildren().stream().sorted((o1, o2) -> {
            if (o1 instanceof TaskButton button1 && o2 instanceof TaskButton button2) {
                return button1.getTitle().compareTo(button2.getTitle());
            }
            return 0;
        }).toList();
        tasksBox.getChildren().clear();
        tasksBox.getChildren().addAll(sorted);
    }

    public VBox getTasksBox() {
        return tasksBox;
    }

    public void registerDisplay(Display display) {
        displays.add(display);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        displays.forEach(Display::refresh);
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        TODAY,
        MONTH,
        LAST_RUN
    }
}
