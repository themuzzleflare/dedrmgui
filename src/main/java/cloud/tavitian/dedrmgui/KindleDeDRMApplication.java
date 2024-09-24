/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class KindleDeDRMApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        KindleDeDRMController controller = new KindleDeDRMController();
        StackPane rootStackPane = controller.getRootStackPane();
        Scene scene = new Scene(rootStackPane);

        primaryStage.setTitle(Util.getAppName());
        primaryStage.getIcons().addAll(Util.getAllIconImages());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(532.0);
        primaryStage.setMinHeight(478.0);
        primaryStage.show();
    }
}

