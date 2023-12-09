package com.gzs.tasker;

import com.gzs.tasker.element.ClickableMenu;
import com.gzs.tasker.report.ReportWindow;
import com.gzs.tasker.state.State;
import com.gzs.tasker.state.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.gzs.tasker.TasksDisplayHandler.Mode.*;

public class TaskerApplication extends Application {

    private State state;

    private StateFilesHandler stateFilesHandler;
    private TasksDisplayHandler tasksDisplayHandler;

    @Override
    public void start(Stage stage) {
        stateFilesHandler = new StateFilesHandler();
        state = stateFilesHandler.loadSavedState();

        tasksDisplayHandler = new TasksDisplayHandler();

        initTasksDisplay();
        tasksDisplayHandler.sortTasksButtons();

        Scene scene = initBaseElements();
        initStage(stage, scene);
    }

    void initTasksDisplay() {
        state.getTasks().forEach(tasksDisplayHandler::createTaskButton);
    }

    private Scene initBaseElements() {
        // elements creation
        MenuBar menuBar = new MenuBar();
        Menu addTaskMenuButton = new ClickableMenu("Add");
        Menu timeDisplayMenu = initTimeDisplayMenu();

        VBox panelBox = new VBox();
        panelBox.getChildren().addAll(menuBar, tasksDisplayHandler.getTasksBox());
        Scene scene = new Scene(panelBox, 100, 600);

        // elements assignment
        menuBar.getMenus().addAll(addTaskMenuButton, timeDisplayMenu);

        // elements actions
        addTaskMenuButton.setOnAction(ev -> {
            Task task = state.addTask("Task " + tasksDisplayHandler.getTasksBox().getChildren().size());
            tasksDisplayHandler.createTaskButton(task);
        });
        return scene;
    }

    private Menu initTimeDisplayMenu() {
        // initialization
        Menu timeDisplayMenu = new Menu("Time");
        RadioMenuItem todayOptionItem = new RadioMenuItem("Today");
        RadioMenuItem recentOptionItem = new RadioMenuItem("Recent");
        RadioMenuItem allOptionItem = new RadioMenuItem("All");
        ToggleGroup toggleGroup = new ToggleGroup();
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem timeReportItem = new MenuItem("Time Report");

        todayOptionItem.setSelected(true);

        // assignment
        toggleGroup.getToggles().addAll(todayOptionItem, recentOptionItem, allOptionItem);
        timeDisplayMenu.getItems().addAll(todayOptionItem, recentOptionItem, allOptionItem, separator, timeReportItem);

        // actions
        todayOptionItem.setOnAction(ev -> tasksDisplayHandler.setMode(TODAY));
        allOptionItem.setOnAction(ev -> tasksDisplayHandler.setMode(FULL));
        recentOptionItem.setOnAction(ev -> tasksDisplayHandler.setMode(LAST_RUN));
        timeReportItem.setOnAction(ev -> new ReportWindow(state));

        return timeDisplayMenu;
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