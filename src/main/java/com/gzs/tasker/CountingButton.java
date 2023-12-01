package com.gzs.tasker;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

class CountingButton extends ToggleButton {
    private final int PRIMARY_BUTTON_ORDINAL = 1;
    private final int MIDDLE_BUTTON_ORDINAL = 2;
    private final int SECONDARY_BUTTON_ORDINAL = 3;

    TextField textEditField = new TextField();
    VBox editLayout = new VBox();

    String text;
    int timerValue = 0;

    Timeline timeline = new Timeline(
            new KeyFrame(
                    Duration.seconds(1),
                    ev -> {
                        timerValue++;
                        updateTextWithCounter();
                    }));

    public CountingButton(String initialText) {
        this.text = initialText;
        updateTextWithCounter();
        setTextAlignment(TextAlignment.CENTER);
        timeline.setCycleCount(Animation.INDEFINITE);

        editLayout.setSpacing(2);

        editLayout.getChildren().add(textEditField);
        Button resetButton = new Button("Reset timer");
        editLayout.getChildren().add(resetButton);
        resetButton.setOnMouseClicked(e -> {
            stop();
            this.setSelected(false);
            timerValue = 0;
            updateTextWithCounter();
        });

        setOnMouseClicked(e -> {
            switch (e.getButton().ordinal()) {
                case PRIMARY_BUTTON_ORDINAL:
                    getToggleGroup().getToggles()
                            .forEach(toggle -> {
                                if (toggle instanceof CountingButton countingButton) {
                                    if (countingButton.isSelected()) {
                                        countingButton.play();
                                    } else {
                                        countingButton.stop();
                                    }
                                }
                            });
                    break;
                case MIDDLE_BUTTON_ORDINAL:
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Delete?", ButtonType.YES, ButtonType.CANCEL);
                    Node node = (Node) e.getSource();
                    Stage thisStage = (Stage) node.getScene().getWindow();
                    alert.initOwner(thisStage);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES && (getParent() instanceof Pane pane)) {
                        this.setToggleGroup(null);
                        pane.getChildren().remove(this);
                    }
                    break;
                case SECONDARY_BUTTON_ORDINAL:
                    editLayout.setPrefWidth(this.getWidth() - 20);
                    textEditField.setText(text);
                    setGraphic(editLayout);
                    break;
                default:
                    break;
            }
        });

        textEditField.setOnAction(actionEvent -> {
            text = textEditField.getText();
            setGraphic(null);
            updateTextWithCounter();
        });
    }

    public void play() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void updateTextWithCounter() {
        String secondLineText = String.format("%02d:%02d:%02d", (timerValue / 60) / 60, (timerValue / 60) % 60, timerValue % 60);
        setText(text + "\n" + secondLineText);
    }
}
