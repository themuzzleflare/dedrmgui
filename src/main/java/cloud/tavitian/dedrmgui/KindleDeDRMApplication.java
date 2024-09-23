/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public final class KindleDeDRMApplication extends Application {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        launch(args);
    }

    private static Image[] getIcons() {
        String path;

        if (OS_NAME.startsWith("mac") || OS_NAME.startsWith("darwin")) {
            path = "macos";
        } else {
            path = "all";
        }

        String[] filenames = {
                "16x16.png",
                "16x16@2x.png",
                "32x32.png",
                "32x32@2x.png",
                "128x128.png",
                "128x128@2x.png",
                "256x256.png",
                "256x256@2x.png",
                "512x512.png",
                "512x512@2x.png"
        };

        InputStream[] iconStreams = Arrays.stream(filenames)
                .map(filename -> String.format("%s/%s", path, filename))
                .map(KindleDeDRMApplication.class::getResourceAsStream)
                .filter(Objects::nonNull)
                .toArray(InputStream[]::new);

        return Arrays.stream(iconStreams).map(Image::new).toArray(Image[]::new);
    }

    @Override
    public void start(Stage stage) {
        KindleDeDRMController controller = new KindleDeDRMController();
        VBox rootVBox = controller.getRootVBox();
        Scene scene = new Scene(rootVBox);

        Image[] icons = getIcons();

        stage.setTitle("Kindle DeDRM");
        stage.getIcons().addAll(icons);
        stage.setScene(scene);
        stage.setMinWidth(532.0);
        stage.setMinHeight(478.0);
        stage.show();
    }
}

