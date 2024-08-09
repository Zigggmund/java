module com.example.health_of_programmer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;
    requires com.google.gson;
    requires org.controlsfx.controls;
    exports app;

    opens app to javafx.fxml;
}