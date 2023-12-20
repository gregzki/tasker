package com.gzs.tasker.element;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.gzs.tasker.util.TimerFormatter.formatCountValue;

class CountingButton extends ToggleButton {

    private static final int PRIMARY_BUTTON_ORDINAL = 1;

    private final TextField textEditField = new TextField();
    private final VBox editLayout = new VBox();

    String text;
    Long timerValue = 0L;
    Timeline timeline;

    public CountingButton(String initialText) {
        setTitle(initialText);
        updateTextWithCounter();
        setTextAlignment(TextAlignment.CENTER);

        initEditLayout();
        initContextMenu();
        initCounter();

        initClickActions();
    }

    private void initEditLayout() {
        initTextField();

        editLayout.setSpacing(2);
        editLayout.getChildren().add(textEditField);
    }

    private void initTextField() {
        textEditField.setAlignment(Pos.CENTER);
        textEditField.setOnAction(ae -> setButtonTitle());
    }

    private void initContextMenu() {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem sub5minutes = new MenuItem("-5 minutes");
        MenuItem add5minutes = new MenuItem("+5 minutes");
        MenuItem sub15minutes = new MenuItem("-15 minutes");
        MenuItem add15minutes = new MenuItem("+15 minutes");
        MenuItem sub1hour = new MenuItem("-1 hour");
        MenuItem add1hour = new MenuItem("+1 hour");

        MenuItem editTitle = new MenuItem("Edit title");

        MenuItem removeThis = new MenuItem("Remove");

        contextMenu.getItems().addAll(
                sub5minutes,
                add5minutes,
                sub15minutes,
                add15minutes,
                sub1hour,
                add1hour,
                separator(),
                editTitle,
                separator(),
                removeThis);

        sub5minutes.setOnAction(e -> addMinutes(-5));
        add5minutes.setOnAction(e -> addMinutes(5));
        sub15minutes.setOnAction(e -> addMinutes(-15));
        add15minutes.setOnAction(e -> addMinutes(15));
        sub1hour.setOnAction(e -> addMinutes(-60));
        add1hour.setOnAction(e -> addMinutes(60));
        editTitle.setOnAction(e -> {
            editLayout.setPrefWidth(this.getWidth() - 20);
            textEditField.setText(text);
            setGraphic(editLayout);
        });
        removeThis.setOnAction(e -> {
            if (deleteConfirmation()) {
                removeThisButton();
            }
        });


        this.setContextMenu(contextMenu);
    }

    private static SeparatorMenuItem separator() {
        return new SeparatorMenuItem();
    }

    void resetCounter() {
        stop();
        this.setSelected(false);
        timerValue = 0L;
        setButtonTitle();
    }

    private void setButtonTitle() {
        setTitle(textEditField.getText());
        setGraphic(null);
        updateTextWithCounter();
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
            if (e.getButton().ordinal() == PRIMARY_BUTTON_ORDINAL) {
                getToggleGroup().getToggles().forEach(toggle -> {
                    if (toggle instanceof CountingButton countingButton) {
                        toggleCounting(countingButton);
                    }
                });
            }
        });
    }

    void removeThisButton() {
        if ((getParent() instanceof Pane pane)) {
            this.setToggleGroup(null);
            pane.getChildren().remove(this);
        }
    }

    private static void toggleCounting(CountingButton countingButton) {
        if (countingButton.isSelected()) {
            countingButton.play();
        } else {
            countingButton.stop();
        }
    }

    private boolean deleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Delete?", ButtonType.YES, ButtonType.CANCEL);
        Stage thisStage = (Stage) this.getScene().getWindow();
        alert.initOwner(thisStage);
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    void updateTextWithCounter() {
        String secondLineText = formatCountValue(timerValue);
        setText(text + "\n" + secondLineText);
    }

    public void play() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void setTitle(String title) {
        this.text = title;
    }

    public String getTitle() {
        return this.text;
    }

    void addMinutes(int value) {
        long seconds = value * 60L;
        timerValue += seconds;
        if (timerValue < 0) {
            timerValue = 0L;
        }
        updateTextWithCounter();
    }
}
