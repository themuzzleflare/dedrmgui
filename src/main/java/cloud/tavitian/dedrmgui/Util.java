/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

final class Util {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String APP_NAME = "Kindle DeDRM";
    private static final String ROOT_ICONS_PATH = "icons";
    private static final String ROOT_FONTS_PATH = "fonts";
    private static final String MACOS_ICONS_PATH = "macos";
    private static final String ALL_ICONS_PATH = "all";
    private static final String INPUT_MONO_FONT_FILENAME = "InputMono-Regular.ttf";
    private static final Font INPUT_MONO_FONT = Font.loadFont(Util.class.getResourceAsStream(String.format("%s/%s", ROOT_FONTS_PATH, INPUT_MONO_FONT_FILENAME)), 12);
    private static final int[] STANDARD_ICON_SIZES = {16, 32, 128, 256, 512};
    private static final int[] RETINA_ICON_SIZES = {16, 32, 128, 256, 512};

    private Util() {
    }

    public static String getAppName() {
        return APP_NAME;
    }

    private static boolean isMacOS() {
        return OS_NAME.startsWith("mac") || OS_NAME.startsWith("darwin");
    }

    @SuppressWarnings("unused")
    private static boolean isWindows() {
        return OS_NAME.startsWith("win");
    }

    @SuppressWarnings("unused")
    private static boolean isLinux() {
        return OS_NAME.startsWith("linux");
    }

    private static String[] getStandardIconFilenames() {
        return Arrays.stream(STANDARD_ICON_SIZES)
                .mapToObj(size -> String.format("%dx%d.png", size, size))
                .toArray(String[]::new);
    }

    private static String[] getRetinaIconFilenames() {
        return Arrays.stream(RETINA_ICON_SIZES)
                .mapToObj(size -> String.format("%dx%d@2x.png", size, size))
                .toArray(String[]::new);
    }

    private static String[] getAllIconFilenames() {
        return Stream.concat(Arrays.stream(getStandardIconFilenames()), Arrays.stream(getRetinaIconFilenames()))
                .toArray(String[]::new);
    }

    private static String getIconsPathForOS() {
        if (isMacOS()) {
            return MACOS_ICONS_PATH;
        } else {
            return ALL_ICONS_PATH;
        }
    }

    private static InputStream[] getAllIconInputStreams() {
        String[] iconFilenames = getAllIconFilenames();
        String osIconsPath = getIconsPathForOS();

        return Arrays.stream(iconFilenames)
                .map(filename -> Util.class.getResourceAsStream(String.format("%s/%s/%s", ROOT_ICONS_PATH, osIconsPath, filename)))
                .filter(Objects::nonNull)
                .toArray(InputStream[]::new);
    }

    public static Image[] getAllIconImages() {
        return Arrays.stream(getAllIconInputStreams())
                .map(Image::new)
                .toArray(Image[]::new);
    }

    public static Font consoleOutputFont() {
        if (INPUT_MONO_FONT == null) {
            return Font.font("Monospaced", 12);
        }

        return INPUT_MONO_FONT;
    }
}
