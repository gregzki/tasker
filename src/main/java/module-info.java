module com.gzs.tasker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    exports com.gzs.tasker;
    opens com.gzs.tasker to javafx.fxml;
    exports com.gzs.tasker.element;
    opens com.gzs.tasker.element to javafx.fxml;
    exports com.gzs.tasker.state;
    opens com.gzs.tasker.state to javafx.fxml;
}