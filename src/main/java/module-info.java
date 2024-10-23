module dedrmgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires dedrmtools;
    requires com.google.gson;
    requires org.jetbrains.annotations;
    requires nsmenufx;

    opens cloud.tavitian.dedrmgui to javafx.fxml, com.google.gson;

    exports cloud.tavitian.dedrmgui;
}
