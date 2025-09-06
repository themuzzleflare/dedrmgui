/*
 * Copyright © 2024-2025 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

public final class KindleDeDRMApplication extends Application {
    private Closeable controller;

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage primaryStage) throws IOException {
        Util.setSystemMenuBar();

        Scene scene;

        if (Util.isUsingFXML()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("kindlededrm-view.fxml"));
            scene = new Scene(fxmlLoader.load());
            controller = fxmlLoader.getController();
        } else {
            KindleDeDRMController controller = new KindleDeDRMController();
            Pane rootPane = controller.getRootPane();
            scene = new Scene(rootPane);
            this.controller = controller;
        }

        primaryStage.setTitle(Util.getAppName());
        primaryStage.getIcons().addAll(Util.getAllIconImages());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(532.0);
        primaryStage.setMinHeight(478.0);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (controller != null) controller.close();
        super.stop();
    }
}

