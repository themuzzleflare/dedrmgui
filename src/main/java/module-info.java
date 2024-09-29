module dedrmgui {
    requires javafx.controls;
    requires dedrmtools;
    requires com.google.gson;
    requires org.jetbrains.annotations;
    requires nsmenufx;

    opens cloud.tavitian.dedrmgui to com.google.gson;

    exports cloud.tavitian.dedrmgui;
}
