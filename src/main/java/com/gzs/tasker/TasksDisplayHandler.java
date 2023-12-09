package com.gzs.tasker;

import com.gzs.tasker.element.TaskButton;
import com.gzs.tasker.state.Task;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TasksDisplayHandler {

    private final VBox tasksBox;

    private final ToggleGroup tasksGroup = new ToggleGroup();

    private final Set<Display> displays = new HashSet<>();
    private Mode mode = Mode.TODAY;

    public TasksDisplayHandler() {
        this.tasksBox = new VBox();
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

    public void removeTask(TaskButton button) {
        tasksBox.getChildren().remove(button);
        sortTasksButtons();
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
        FULL,
        LAST_RUN
    }
}
