/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class KindleDeDRMApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new KindleDeDRMController());
        stage.setTitle("Kindle DeDRM");
        stage.setScene(scene);
        stage.show();
    }
}

