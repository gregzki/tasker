package com.gzs.tasker;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TaskerApplication extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = initBaseElements();

        initStage(stage, scene);
    }

    private static Scene initBaseElements() {
        // elements creation
        MenuBar menuBar = new MenuBar();
        Menu addTaskMenuButton = new ClickableMenu("Add");
        Menu timeDisplayMenu = initTimeDisplayMenu();

        VBox box = new VBox(menuBar);
        ToggleGroup group = new ToggleGroup();
        Scene scene = new Scene(box, 100, 600);

        // elements assignment
        menuBar.getMenus().addAll(addTaskMenuButton, timeDisplayMenu);

        // elements actions
        addTaskMenuButton.setOnAction(e -> {
            ObservableList<Node> boxChildren = box.getChildren();
            ToggleButton button = createButton("Task " + boxChildren.size(), group);
            boxChildren.add(button);
        });
        return scene;
    }

    private static Menu initTimeDisplayMenu() {
        Menu timeDisplayMenu = new Menu("Time");
        RadioMenuItem choice1Item = new RadioMenuItem("Today");
        choice1Item.setSelected(true);
        RadioMenuItem choice2Item = new RadioMenuItem("All");
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(choice1Item, choice2Item);
        timeDisplayMenu.getItems().addAll(choice1Item, choice2Item);
        return timeDisplayMenu;
    }

    private static ToggleButton createButton(String text, ToggleGroup group) {
        ToggleButton button = new CountingButton(text);
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        button.setToggleGroup(group);
        return button;
    }

    private static void initStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Tasker");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}