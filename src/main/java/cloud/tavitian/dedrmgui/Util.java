/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.nsmenufx.MenuToolkit;
import cloud.tavitian.nsmenufx.dialogs.about.AboutStageBuilder;
import cloud.tavitian.nsmenufx.icns.IcnsParser;
import cloud.tavitian.nsmenufx.icns.IcnsType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

final class Util {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase();
    private static final String APP_NAME = "Kindle DeDRM";
    private static final String APP_DESCRIPTION = "Remove DRM from Amazon Kindle eBooks";
    private static final String APP_COPYRIGHT = "Copyright © 2024 Paul Tavitian";
    private static final String APP_VERSION = "8.0.0";
    private static final String ROOT_ICONS_PATH = "icons";
    private static final String ROOT_FONTS_PATH = "fonts";
    private static final String MACOS_ICONS_PATH = "macos";
    private static final String ALL_ICONS_PATH = "all";
    private static final String INPUT_MONO_FONT_FILENAME = "InputMono-Regular.ttf";
    private static final Font INPUT_MONO_FONT = Font.loadFont(Util.class.getResourceAsStream(String.format("%s/%s", ROOT_FONTS_PATH, INPUT_MONO_FONT_FILENAME)), 12);
    private static final int[] STANDARD_ICON_SIZES = {16, 32, 128, 256, 512};
    private static final int[] RETINA_ICON_SIZES = {16, 32, 128, 256, 512};
    private static final MenuToolkit MENU_TOOLKIT = MenuToolkit.toolkit();

    private Util() {
    }

    public static String getAppName() {
        return APP_NAME;
    }

    public static String getAppDescription() {
        return APP_DESCRIPTION;
    }

    public static String getAppCopyright() {
        return APP_COPYRIGHT;
    }

    public static String getAppVersion() {
        return APP_VERSION;
    }

    private static boolean isMacOS() {
        return OS_NAME.startsWith("mac");
    }

    @SuppressWarnings("unused")
    private static boolean isWindows() {
        return OS_NAME.startsWith("win");
    }

    @SuppressWarnings("unused")
    private static boolean isLinux() {
        return OS_NAME.startsWith("linux");
    }

    @SuppressWarnings("unused")
    private static boolean isIntel() {
        return OS_ARCH.equals("x86_64");
    }

    private static boolean isArm() {
        return OS_ARCH.contains("arm") || OS_ARCH.contains("aarch64");
    }

    private static boolean isSilicon() {
        return isArm();
    }

    private static String getStandardIconFilename(int size) {
        return String.format("%dx%d.png", size, size);
    }

    private static String[] getStandardIconFilenames() {
        return Arrays.stream(STANDARD_ICON_SIZES)
                .mapToObj(Util::getStandardIconFilename)
                .toArray(String[]::new);
    }

    private static String getRetinaIconFilename(int size) {
        return String.format("%dx%d@2x.png", size, size);
    }

    private static String[] getRetinaIconFilenames() {
        return Arrays.stream(RETINA_ICON_SIZES)
                .mapToObj(Util::getRetinaIconFilename)
                .toArray(String[]::new);
    }

    private static String getIconFilename(int size) {
        if (Arrays.stream(RETINA_ICON_SIZES).anyMatch(s -> s == size)) {
            return getRetinaIconFilename(size);
        } else {
            return getStandardIconFilename(size);
        }
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

    @SuppressWarnings("unused")
    private static InputStream getStandardIconInputStream(int size) {
        return Util.class.getResourceAsStream(String.format("%s/%s/%s", ROOT_ICONS_PATH, getIconsPathForOS(), getStandardIconFilename(size)));
    }

    @SuppressWarnings("unused")
    private static InputStream getRetinaIconInputStream(int size) {
        return Util.class.getResourceAsStream(String.format("%s/%s/%s", ROOT_ICONS_PATH, getIconsPathForOS(), getRetinaIconFilename(size)));
    }

    private static InputStream getIconInputStream(int size) {
        return Util.class.getResourceAsStream(String.format("%s/%s/%s", ROOT_ICONS_PATH, getIconsPathForOS(), getIconFilename(size)));
    }

    private static InputStream getIconInputStream(String filename) {
        return Util.class.getResourceAsStream(String.format("%s/%s/%s", ROOT_ICONS_PATH, getIconsPathForOS(), filename));
    }

    private static InputStream[] getAllIconInputStreams() {
        return Arrays.stream(getAllIconFilenames())
                .map(Util::getIconInputStream)
                .filter(Objects::nonNull)
                .toArray(InputStream[]::new);
    }

    public static Image[] getAllIconImages() {
        return Arrays.stream(getAllIconInputStreams())
                .filter(Objects::nonNull)
                .map(Image::new)
                .toArray(Image[]::new);
    }

    private static InputStream getLargestIconInputStream() {
        int largestStandardSize = Arrays.stream(STANDARD_ICON_SIZES).max().orElse(0);
        int largestRetinaSize = Arrays.stream(RETINA_ICON_SIZES).max().orElse(0);
        int largestSize = Math.max(largestStandardSize, largestRetinaSize);
        return getIconInputStream(largestSize);
    }

    private static Image getLargestIconImage() {
        InputStream largestIconInputStream = getLargestIconInputStream();

        if (largestIconInputStream == null) {
            return null;
        }

        return new Image(largestIconInputStream);
    }

    private static File getGenericApplicationIconFile() {
        return new File("/System/Library/CoreServices/CoreTypes.bundle/Contents/Resources/GenericApplicationIcon.icns");
    }

    private static InputStream getGenericApplicationIconInputStream() {
        try {
            IcnsParser icnsParser = IcnsParser.forFile(getGenericApplicationIconFile());
            return icnsParser.getIconStream(IcnsType.ic09);
        } catch (IOException _) {
            return null;
        }
    }

    private static Image getGenericApplicationIconImage() {
        InputStream genericApplicationIconInputStream = getGenericApplicationIconInputStream();

        if (genericApplicationIconInputStream == null) {
            return null;
        }

        return new Image(genericApplicationIconInputStream);
    }

    private static Image getAboutIconImage() {
        Image largestIconImage = getLargestIconImage();

        if (largestIconImage == null) {
            return getGenericApplicationIconImage();
        }

        return largestIconImage;
    }

    public static Font consoleOutputFont() {
        if (INPUT_MONO_FONT == null) {
            return Font.font("Monospaced", 12);
        }

        return INPUT_MONO_FONT;
    }

    private static Stage createCustomAboutStage() {
        return AboutStageBuilder
                .start("About " + getAppName())
                .withAppName(getAppName())
                .withImage(getAboutIconImage())
                .withText(getAppDescription())
                .withVersionString(getAppVersion())
                .withCopyright(getAppCopyright())
                .build();
    }

    private static Menu createMacOSIntelApplicationMenu() {
        Menu applicationMenu = MENU_TOOLKIT.createDefaultApplicationMenu(getAppName());
        MenuItem aboutMenuItem = MENU_TOOLKIT.createNativeAboutMenuItem(getAppName());
        applicationMenu.getItems().set(0, aboutMenuItem);
        return applicationMenu;
    }

    private static Menu createDefaultMacOSSiliconApplicationMenu() {
        return MENU_TOOLKIT.createDefaultApplicationMenu(getAppName());
    }

    private static Menu createCustomMacOSSiliconApplicationMenu() {
        return MENU_TOOLKIT.createDefaultApplicationMenu(getAppName(), createCustomAboutStage());
    }

    private static Menu createMacOSApplicationMenu() {
        if (isSilicon()) {
            return createCustomMacOSSiliconApplicationMenu();
        } else {
            return createMacOSIntelApplicationMenu();
        }
    }

    @SuppressWarnings("unused")
    public static void setMacOSIntelMenuBar() {
        MENU_TOOLKIT.setApplicationMenu(createMacOSIntelApplicationMenu());
    }

    @SuppressWarnings("unused")
    public static void setDefaultMacOSSiliconMenuBar() {
        MENU_TOOLKIT.setApplicationMenu(createDefaultMacOSSiliconApplicationMenu());
    }

    @SuppressWarnings("unused")
    public static void setCustomMacOSSiliconMenuBar() {
        MENU_TOOLKIT.setApplicationMenu(createCustomMacOSSiliconApplicationMenu());
    }

    private static void setMacOSMenuBar() {
        MENU_TOOLKIT.setApplicationMenu(createMacOSApplicationMenu());
    }

    public static void setSystemMenuBar() {
        if (isMacOS()) {
            setMacOSMenuBar();
        }
    }
}
