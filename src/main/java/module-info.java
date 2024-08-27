module dedrmgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires dedrmtools;

    opens cloud.tavitian.dedrmgui to javafx.fxml;
    exports cloud.tavitian.dedrmgui;
}
