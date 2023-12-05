package com.gzs.tasker;

import com.gzs.tasker.element.ClickableMenu;
import com.gzs.tasker.element.TaskButton;
import com.gzs.tasker.state.State;
import com.gzs.tasker.state.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.gzs.tasker.DisplayMode.Value.*;

public class TaskerApplication extends Application {

    private State state;

    private VBox tasksBox;

    private final DisplayMode displayMode = new DisplayMode();
    private ToggleGroup tasksGroup = new ToggleGroup();

    private StateFilesHandler stateFilesHandler;

    @Override
    public void start(Stage stage) {
        Scene scene = initBaseElements();

        stateFilesHandler = new StateFilesHandler();
        state = stateFilesHandler.loadSavedState();
        initTasksDisplay();

        initStage(stage, scene);
    }

    private void initTasksDisplay() {
        state.getTasks().forEach(this::createTaskButton);
    }

    private Scene initBaseElements() {
        // elements creation
        MenuBar menuBar = new MenuBar();
        Menu addTaskMenuButton = new ClickableMenu("Add");
        Menu timeDisplayMenu = initTimeDisplayMenu();

        tasksBox = new VBox(menuBar);
        tasksGroup = new ToggleGroup();
        Scene scene = new Scene(tasksBox, 100, 600);

        // elements assignment
        menuBar.getMenus().addAll(addTaskMenuButton, timeDisplayMenu);

        // elements actions
        addTaskMenuButton.setOnAction(ev -> {
            Task task = state.addTask("Task " + tasksBox.getChildren().size());
            createTaskButton(task);
        });
        return scene;
    }

    private Menu initTimeDisplayMenu() {
        Menu timeDisplayMenu = new Menu("Time");
        RadioMenuItem todayOptionItem = new RadioMenuItem("Today");
        todayOptionItem.setSelected(true);
        RadioMenuItem recentOptionItem = new RadioMenuItem("Recent");
        RadioMenuItem allOptionItem = new RadioMenuItem("All");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(todayOptionItem, recentOptionItem, allOptionItem);
        timeDisplayMenu.getItems().addAll(todayOptionItem, recentOptionItem, allOptionItem);

        todayOptionItem.setOnAction(ev -> displayMode.setValue(TODAY));
        allOptionItem.setOnAction(ev -> displayMode.setValue(FULL));
        recentOptionItem.setOnAction(ev -> displayMode.setValue(LAST_RUN));

        return timeDisplayMenu;
    }

    private void createTaskButton(Task task) {
        ToggleButton button = new TaskButton(task);
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        button.setToggleGroup(tasksGroup);
        displayMode.registerDisplay((Display) button);
        tasksBox.getChildren().add(button);
    }

    private void initStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Tasker");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);

        stateFilesHandler.startAutoSave(state);
        stage.setOnCloseRequest(ev -> stateFilesHandler.finishAutoSave(state));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}