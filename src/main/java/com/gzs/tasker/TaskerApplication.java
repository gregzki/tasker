package com.gzs.tasker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TaskerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Menu addButtonMenu = new ClickableMenu("Add");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(addButtonMenu);

        ToggleGroup group = new ToggleGroup();
        ToggleButton button1 = createButton("1", group);
        ToggleButton button2 = createButton("2", group);
        VBox box = new VBox(menuBar);
        box.getChildren().add(button1);
        box.getChildren().add(button2);

        addButtonMenu.setOnAction(e -> {
            ToggleButton button = createButton("new", group);
            box.getChildren().add(button);
        });

        int width = 100;
        int height = 600;
        Scene scene = new Scene(box, width, height);
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