package com.gzs.tasker;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TaskerApplication extends Application {
    @Override
    public void start(Stage stage) {
        // elements creation
        MenuBar menuBar = new MenuBar();
        Menu addTaskMenuButton = new ClickableMenu("Add");
        VBox box = new VBox(menuBar);
        ToggleGroup group = new ToggleGroup();
        Scene scene = new Scene(box, 100, 600);

        // elements assignment
        menuBar.getMenus().add(addTaskMenuButton);

        // elements actions
        addTaskMenuButton.setOnAction(e -> {
            ObservableList<Node> boxChildren = box.getChildren();
            ToggleButton button = createButton("Task " + boxChildren.size(), group);
            boxChildren.add(button);
        });

        initStage(stage, scene);
    }

    private static void initStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Tasker");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);

        stage.show();
    }

    private static ToggleButton createButton(String text, ToggleGroup group) {
        ToggleButton button = new CountingButton(text);
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        button.setToggleGroup(group);
        return button;
    }

    public static void main(String[] args) {
        launch();
    }
}