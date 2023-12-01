package com.gzs.tasker;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class ClickableMenu extends Menu {

    public ClickableMenu(String title) {
        // dummy item is needed to make JavaFX "believe", that menu item was pressed
        MenuItem dummyItem = new MenuItem();
        dummyItem.setVisible(false);
        getItems().add(dummyItem);

        Label label = new Label();
        label.setText(title);
        label.setOnMouseClicked(evt -> {
            // forced child MenuItem click (this item is hidden, so this action is not visible but triggers parent "onAction" event handler anyway)
            dummyItem.fire();
        });
        setGraphic(label);
    }
}