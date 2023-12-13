module com.gzs.tasker {
    requires javafx.controls;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    requires java.desktop;

    exports com.gzs.tasker;
    exports com.gzs.tasker.element;
    exports com.gzs.tasker.state;
    exports com.gzs.tasker.report;
    exports com.gzs.tasker.trayicon;

    opens com.gzs.tasker.state to com.google.gson;
}