package com.gzs.tasker;

import com.gzs.tasker.element.ClickableMenu;
import com.gzs.tasker.report.ReportWindow;
import com.gzs.tasker.state.State;
import com.gzs.tasker.state.Task;
import com.gzs.tasker.trayicon.TaskerTrayIcon;
import javafx.application.Application;
import javafx.application.Platform;
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
        Platform.setImplicitExit(false);
        stateFilesHandler = new StateFilesHandler();
        state = stateFilesHandler.loadSavedState();

        tasksDisplayHandler = new TasksDisplayHandler();

        initTasksDisplay();

        Scene scene = initBaseElements();
        initStage(stage, scene);

        TaskerTrayIcon trayIcon = new TaskerTrayIcon();
        trayIcon.initTrayIcon(
                () -> showStage(stage),
                () -> stateFilesHandler.finishAutoSave(state));
    }

    void initTasksDisplay() {
        state.getActiveTasks().forEach(tasksDisplayHandler::createTaskButton);
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
        addTaskMenuButton.setOnAction(ev -> createTask());
        return scene;
    }

    private void createTask() {
        Task task = state.addTask("Task " + tasksDisplayHandler.getTasksBox().getChildren().size());
        tasksDisplayHandler.createTaskButton(task);
    }

    private Menu initTimeDisplayMenu() {
        // initialization
        Menu timeDisplayMenu = new Menu("Time");
        RadioMenuItem todayOptionItem = new RadioMenuItem("Today");
        RadioMenuItem recentOptionItem = new RadioMenuItem("Recent");
        RadioMenuItem monthOptionItem = new RadioMenuItem("Month");
        ToggleGroup toggleGroup = new ToggleGroup();
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem timeReportItem = new MenuItem("Time Report");

        todayOptionItem.setSelected(true);

        // assignment
        toggleGroup.getToggles().addAll(todayOptionItem, recentOptionItem, monthOptionItem);
        timeDisplayMenu.getItems().addAll(todayOptionItem, recentOptionItem, monthOptionItem, separator, timeReportItem);

        // actions
        todayOptionItem.setOnAction(ev -> tasksDisplayHandler.setMode(TODAY));
        monthOptionItem.setOnAction(ev -> tasksDisplayHandler.setMode(MONTH));
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

        stage.setOnCloseRequest(ev -> stateFilesHandler.finishAutoSave(state));
    }

    private void showStage(Stage stage) {
        stateFilesHandler.startAutoSave(state);
        stage.show();
        stage.toFront();
    }

    public static void main(String[] args) {
        launch();
    }
}