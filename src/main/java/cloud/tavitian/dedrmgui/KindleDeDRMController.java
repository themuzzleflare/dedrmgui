/*
 * Copyright © 2024-2025 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.Debug;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.jetbrains.annotations.Contract;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

final class KindleDeDRMController extends KindleDeDRMBaseController {
    public KindleDeDRMController() {
        initialize(null, null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureBaseProps();
        super.initialize(location, resources);
    }

    private void configureBaseProps() {
        ebookfileLabel = new Label("eBook File");
        ebookfileTextField = new TextField();
        selectEbookfileButton = new Button("Select");

        ebookfileHBox = new HBox(5.0, ebookfileLabel, ebookfileTextField, selectEbookfileButton);

        outputdirLabel = new Label("Output Directory");
        outputdirTextField = new TextField();
        selectOutputdirButton = new Button("Select");
        deriveOutputdirButton = new Button("Derive");

        outputdirHBox = new HBox(5.0, outputdirLabel, outputdirTextField, selectOutputdirButton, deriveOutputdirButton);
        inputOutputVBox = new VBox(5.0, ebookfileHBox, outputdirHBox);

        keyfileLabel = new Label("Key File");
        keyfileTextField = new TextField();
        selectKeyfileButton = new Button("Select");
        generateKeyfileButton = new Button("Generate");

        keyfileHBox = new HBox(5.0, keyfileLabel, keyfileTextField, selectKeyfileButton, generateKeyfileButton);

        serialLabel = new Label("Serial");
        serialTextField = new TextField();

        serialHBox = new HBox(5.0, serialLabel, serialTextField);
        keyOrSerialRequiredLabel = new Label("Either a Key File or Serial Number must be provided.");
        keySerialVBox = new VBox(5.0, keyfileHBox, serialHBox, keyOrSerialRequiredLabel);
        decryptButton = new Button("Decrypt");
        verboseCheckbox = new CheckBox("Verbose");

        decryptVerboseHBox = new HBox(5.0, decryptButton, verboseCheckbox);
        clearLogsButton = new Button("Clear Logs");
        consoleOutputTextArea = new TextArea();
        decryptVerboseClearLogsHBox = new HBox(5.0, decryptVerboseHBox, new Spacer(), clearLogsButton);
        consoleDecryptVBox = new VBox(5.0, consoleOutputTextArea, decryptVerboseClearLogsHBox);
        taOutputStream = new BufferedTextAreaOutputStream(consoleOutputTextArea);
        printStream = new PrintStream(taOutputStream, true);

        saveSettingsButton = new Button("Save Settings");
        loadSettingsButton = new Button("Load Settings");
        resetSettingsButton = new Button("Reset");

        settingsHBox = new HBox(5.0, saveSettingsButton, loadSettingsButton, resetSettingsButton);

        decryptMenuItem = new MenuItem("Decrypt");
        generateKeyfileMenuItem = new MenuItem("Generate Keyfile");
        clearLogsMenuItem = new MenuItem("Clear Logs");

        saveSettingsMenuItem = new MenuItem("Save Settings");
        loadSettingsMenuItem = new MenuItem("Load Settings");
        resetSettingsMenuItem = new MenuItem("Reset Settings");

        runMenu = new Menu("Run", null, decryptMenuItem, generateKeyfileMenuItem, clearLogsMenuItem);
        settingsMenu = new Menu("Settings", null, saveSettingsMenuItem, loadSettingsMenuItem, resetSettingsMenuItem);

        menuBar = new MenuBar(runMenu, settingsMenu);

        rootVBox = new VBox(20.0, inputOutputVBox, keySerialVBox, settingsHBox, consoleDecryptVBox);
        rootVBoxWithMenuBar = new VBox(menuBar, rootVBox);
        semiTransparentOverlay = new Rectangle();
        progressIndicator = new ProgressIndicator();
        rootStackPane = new StackPane(rootVBoxWithMenuBar, semiTransparentOverlay, progressIndicator);
    }

    @Override
    protected void configureMenuBar() {
        Platform.runLater(() -> menuBar.setUseSystemMenuBar(true));
    }

    @Override
    protected void configureDecryptMenuItem() {
        decryptMenuItem.setOnAction(_ -> decryptBookThrowing());
        decryptMenuItem.disableProperty().bind(decryptDisabled);
        // Set the keyboard shortcut for the menu item, which is Cmd+D on macOS and Ctrl+D on Windows/Linux
        decryptMenuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+D"));
    }

    @Override
    protected void configureGenerateKeyfileMenuItem() {
        generateKeyfileMenuItem.setOnAction(_ -> generateKeyfileThrowing());
        generateKeyfileMenuItem.disableProperty().bind(isGeneratingKeyfile);
        // Set the keyboard shortcut for the menu item, which is Cmd+K on macOS and Ctrl+K on Windows/Linux
        generateKeyfileMenuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+K"));
    }

    @Override
    protected void configureClearLogsMenuItem() {
        clearLogsMenuItem.setOnAction(_ -> clearLogs());
        clearLogsMenuItem.disableProperty().bind(clearLogsDisabled);
        // Set the keyboard shortcut for the menu item, which is Cmd+L on macOS and Ctrl+L on Windows/Linux
        clearLogsMenuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+L"));
    }

    @Override
    protected void configureSaveSettingsMenuItem() {
        saveSettingsMenuItem.setOnAction(_ -> saveSettings());
        saveSettingsMenuItem.disableProperty().bind(saveResetDisabled);
        // Set the keyboard shortcut for the menu item, which is Cmd+S on macOS and Ctrl+S on Windows/Linux
        saveSettingsMenuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
    }

    @Override
    protected void configureLoadSettingsMenuItem() {
        loadSettingsMenuItem.setOnAction(_ -> loadSettings());
        // Set the keyboard shortcut for the menu item, which is Cmd+O on macOS and Ctrl+O on Windows/Linux
        loadSettingsMenuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
    }

    @Override
    protected void configureResetSettingsMenuItem() {
        resetSettingsMenuItem.setOnAction(_ -> resetSettings());
        resetSettingsMenuItem.disableProperty().bind(saveResetDisabled);
        // Set the keyboard shortcut for the menu item, which is Cmd+R on macOS and Ctrl+R on Windows/Linux
        resetSettingsMenuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+R"));
    }

    @Override
    protected void configureRootVBox() {
        rootVBox.setPadding(new Insets(10.0));
        rootVBox.disableProperty().bind(showProgress);

        VBox.setVgrow(rootVBox, Priority.ALWAYS);
    }

    @Override
    protected void configureProgressIndicator() {
        progressIndicator.visibleProperty().bind(showProgress);
        progressIndicator.managedProperty().bind(showProgress); // Ensures it doesn't take up space when hidden
    }

    @Override
    protected void configureSemiTransparentOverlay() {
        semiTransparentOverlay.setFill(Color.BLACK);
        semiTransparentOverlay.setOpacity(0.5);
        semiTransparentOverlay.widthProperty().bind(rootStackPane.widthProperty());
        semiTransparentOverlay.heightProperty().bind(rootStackPane.heightProperty());
        semiTransparentOverlay.visibleProperty().bind(showProgress);
        semiTransparentOverlay.managedProperty().bind(showProgress);
    }

    @Override
    protected void configureConsoleDecryptVBox() {
        VBox.setVgrow(consoleDecryptVBox, Priority.ALWAYS);
    }

    @Override
    protected void configureDecryptVerboseClearLogsHBox() {
        decryptVerboseClearLogsHBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void configureDecryptVerboseHBox() {
        decryptVerboseHBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void configureClearLogsButton() {
        clearLogsButton.setOnAction(_ -> clearLogs());
        clearLogsButton.disableProperty().bind(clearLogsDisabled);
    }

    @Override
    protected void configureVerboseCheckbox() {
        verboseCheckbox.setOnAction(_ -> Debug.setEnabled(verboseCheckbox.isSelected()));
    }

    @Override
    protected void configureConsoleOutputTextArea() {
        consoleOutputTextArea.setEditable(false);
        consoleOutputTextArea.setWrapText(true);
        consoleOutputTextArea.setPromptText("Console Output");
        consoleOutputTextArea.setFont(Util.consoleOutputFont());

        VBox.setVgrow(consoleOutputTextArea, Priority.ALWAYS);
    }

    @Override
    protected void configureDecryptButton() {
        decryptButton.setOnAction(_ -> decryptBookThrowing());
        decryptButton.disableProperty().bind(decryptDisabled);
    }

    @Override
    protected void configureSettingsHBox() {
        settingsHBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void configureResetSettingsButton() {
        resetSettingsButton.setOnAction(_ -> resetSettings());
        resetSettingsButton.disableProperty().bind(saveResetDisabled);
    }

    @Override
    protected void configureLoadSettingsButton() {
        loadSettingsButton.setOnAction(_ -> loadSettings());
    }

    @Override
    protected void configureSaveSettingsButton() {
        saveSettingsButton.setOnAction(_ -> saveSettings());
        saveSettingsButton.disableProperty().bind(saveResetDisabled);
    }

    @Override
    protected void configureKeySerialVBox() {
        keySerialVBox.setAlignment(Pos.CENTER);
        keySerialVBox.setPadding(new Insets(5.0));
        keySerialVBox.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, new CornerRadii(5.0), new BorderWidths(1.0))));
    }

    @Override
    protected void configureKeyOrSerialRequiredLabel() {
        keyOrSerialRequiredLabel.setTextAlignment(TextAlignment.CENTER);
        keyOrSerialRequiredLabel.visibleProperty().bind(keyOrSerialRequiredVisible);
        keyOrSerialRequiredLabel.managedProperty().bind(keyOrSerialRequiredVisible);
        keyOrSerialRequiredLabel.setFont(Font.font(12.0));
    }

    @Override
    protected void configureSerialHBox() {
        serialHBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void configureSerialTextField() {
        serialTextField.setPromptText("Kindle Serial Number");

        HBox.setHgrow(serialTextField, Priority.ALWAYS);
    }

    @Override
    protected void configureKeyfileHBox() {
        keyfileHBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void configureGenerateKeyfileButton() {
        Tooltip tooltip = new Tooltip("Attempt to generate a key file based on the present installation of Kindle for PC/Mac.");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        tooltip.setShowDuration(Duration.INDEFINITE);

        generateKeyfileButton.setTooltip(tooltip);
        generateKeyfileButton.setOnAction(_ -> generateKeyfileThrowing());
    }

    @Override
    protected void configureSelectKeyfileButton() {
        selectKeyfileButton.setOnAction(_ -> selectKeyfile());
    }

    @Override
    protected void configureKeyfileTextField() {
        keyfileTextField.setPromptText(".k4i File");

        HBox.setHgrow(keyfileTextField, Priority.ALWAYS);
    }

    @Override
    protected void configureInputOutputVBox() {
        inputOutputVBox.setAlignment(Pos.CENTER);
        inputOutputVBox.setPadding(new Insets(5.0));
        inputOutputVBox.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, new CornerRadii(5.0), new BorderWidths(1.0))));
    }

    @Override
    protected void configureOutputdirHBox() {
        outputdirHBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void configureDeriveOutputdirButton() {
        deriveOutputdirButton.setOnAction(_ -> deriveOutputdir());
        deriveOutputdirButton.disableProperty().bind(deriveOutputdirDisabled);
    }

    @Override
    protected void configureSelectOutputdirButton() {
        selectOutputdirButton.setOnAction(_ -> selectOutputdir());
    }

    @Override
    protected void configureOutputdirTextField() {
        outputdirTextField.setPromptText("Output Directory");

        HBox.setHgrow(outputdirTextField, Priority.ALWAYS);
    }

    @Override
    protected void configureEbookfileHBox() {
        ebookfileHBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void configureSelectEbookfileButton() {
        selectEbookfileButton.setOnAction(_ -> selectEbookfile());
    }

    @Override
    protected void configureEbookfileTextField() {
        ebookfileTextField.setPromptText("eBook File");

        HBox.setHgrow(ebookfileTextField, Priority.ALWAYS);
    }

    @Contract(pure = true)
    public Pane getRootPane() {
        return rootStackPane;
    }
}
