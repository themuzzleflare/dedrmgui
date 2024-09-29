/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public final class KindleDeDRMApplication extends Application {
    private static final KindleDeDRMController CONTROLLER = new KindleDeDRMController();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage primaryStage) {
        Util.setSystemMenuBar();

        Pane rootPane = CONTROLLER.getRootPane();
        Scene scene = new Scene(rootPane);

        primaryStage.setTitle(Util.getAppName());
        primaryStage.getIcons().addAll(Util.getAllIconImages());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(532.0);
        primaryStage.setMinHeight(478.0);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        CONTROLLER.close();
        super.stop();
    }
}

