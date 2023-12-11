package com.gzs.tasker.report;

import com.gzs.tasker.state.State;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

import static com.gzs.tasker.util.TimerFormatter.formatCountValue;

public class ReportWindow {

    private final List<DataRow> rows;

    public ReportWindow(State state) {
        rows = transformDataToRows(state);
        Scene scene = initBaseElements();

        initStage(scene);
    }

    private List<DataRow> transformDataToRows(State state) {
        return state.getTasks().stream()
                .flatMap(task -> task.getReportedDays().stream()
                        .map(day -> new DataRow(
                                day.toString(),
                                task.getTitle(),
                                formatCountValue(task.computeDayCount(day)))
                        ))
                .toList();
    }

    private Scene initBaseElements() {
        // elements creation
        TableView<DataRow> tableView = new TableView<>();
        TableColumn<DataRow, String> column1 = new TableColumn<>("Day");
        column1.setCellValueFactory(new PropertyValueFactory<>("day"));
        TableColumn<DataRow, String> column2 = new TableColumn<>("Task");
        column2.setCellValueFactory(new PropertyValueFactory<>("task"));
        TableColumn<DataRow, String> column3 = new TableColumn<>("Time Reported");
        column3.setCellValueFactory(new PropertyValueFactory<>("time"));

        tableView.getColumns().addAll(column1, column2, column3);
        tableView.getSortOrder().addAll(column1, column2);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // data
        tableView.getItems().addAll(rows);

        column1.setSortType(TableColumn.SortType.DESCENDING);
        tableView.sort();

        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox vbox = new VBox(tableView);
        return new Scene(vbox, 640, 480);
    }

    private void initStage(Scene scene) {
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle("Tasker - Time Report");
        stage.setAlwaysOnTop(true);

        stage.show();
    }
}
