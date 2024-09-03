module dedrmgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires dedrmtools;
    requires com.google.gson;

    opens cloud.tavitian.dedrmgui to javafx.fxml, com.google.gson;

    exports cloud.tavitian.dedrmgui;
}
