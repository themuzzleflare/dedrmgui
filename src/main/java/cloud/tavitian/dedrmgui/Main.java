/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new KindleDeDRMController(), 1000, 1000);
        stage.setTitle("KindleDeDRM");
        stage.setScene(scene);
        stage.show();
    }
}

