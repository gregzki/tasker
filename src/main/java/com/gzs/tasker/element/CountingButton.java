package com.gzs.tasker.element;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

class CountingButton extends ToggleButton {
    private static final int PRIMARY_BUTTON_ORDINAL = 1;
    private static final int MIDDLE_BUTTON_ORDINAL = 2;
    private static final int SECONDARY_BUTTON_ORDINAL = 3;

    private final TextField textEditField = new TextField();
    private final VBox editLayout = new VBox();

    String text;
    Long timerValue = 0L;
    Timeline timeline;

    public CountingButton(String initialText, Long timerValue) {
        this(initialText);
        this.timerValue = timerValue;
    }

    public CountingButton(String initialText) {
        this.text = initialText;
        updateTextWithCounter();
        setTextAlignment(TextAlignment.CENTER);

        initEditLayout();
        initCounter();

        initClickActions();
    }

    private void initEditLayout() {
        editLayout.setSpacing(2);
        editLayout.getChildren().add(textEditField);

        initResetButton();
        initTextField();
    }

    private void initResetButton() {
        Button resetButton = new Button("Reset timer");
        editLayout.getChildren().add(resetButton);
        resetButton.setOnMouseClicked(e -> {
            stop();
            this.setSelected(false);
            timerValue = 0L;
            updateTextWithCounter();
        });
    }

    private void initTextField() {
        textEditField.setOnAction(actionEvent -> {
            text = textEditField.getText();
            setGraphic(null);
            updateTextWithCounter();
        });
    }

    private void initCounter() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), this::timerTickHandler));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    void timerTickHandler(ActionEvent ev) {
        timerValue++;
        updateTextWithCounter();
    }

    private void initClickActions() {
        setOnMouseClicked(e -> {
            switch (e.getButton().ordinal()) {
                case PRIMARY_BUTTON_ORDINAL:
                    getToggleGroup().getToggles().forEach(toggle -> {
                        if (toggle instanceof CountingButton countingButton) {
                            toggleCounting(countingButton);
                        }
                    });
                    break;
                case MIDDLE_BUTTON_ORDINAL:
                    if (deleteConfirmation(e) && (getParent() instanceof Pane pane)) {
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
    }

    private static void toggleCounting(CountingButton countingButton) {
        if (countingButton.isSelected()) {
            countingButton.play();
        } else {
            countingButton.stop();
        }
    }

    private boolean deleteConfirmation(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Delete?", ButtonType.YES, ButtonType.CANCEL);
        Node node = (Node) e.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        alert.initOwner(thisStage);
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    public void play() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    void updateTextWithCounter() {
        String secondLineText = String.format("%02d:%02d:%02d", (timerValue / 60) / 60, (timerValue / 60) % 60, timerValue % 60);
        setText(text + "\n" + secondLineText);
    }
}
