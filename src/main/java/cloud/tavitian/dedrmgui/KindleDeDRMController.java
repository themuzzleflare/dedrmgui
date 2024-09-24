/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.DeDRM;
import cloud.tavitian.dedrmtools.Debug;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class KindleDeDRMController {
    private final Label ebookfileLabel = new Label("eBook File");
    private final TextField ebookfileTextField = new TextField();
    private final Button selectEbookfileButton = new Button("Select");
    private final HBox ebookfileHBox = new HBox(5.0, ebookfileLabel, ebookfileTextField, selectEbookfileButton);
    private final Label outputdirLabel = new Label("Output Directory");
    private final TextField outputdirTextField = new TextField();
    private final Button selectOutputdirButton = new Button("Select");
    private final Button deriveOutputdirButton = new Button("Derive");
    private final HBox outputdirHBox = new HBox(5.0, outputdirLabel, outputdirTextField, selectOutputdirButton, deriveOutputdirButton);
    private final VBox inputOutputVBox = new VBox(5.0, ebookfileHBox, outputdirHBox);
    private final Label keyfileLabel = new Label("Key File");
    private final TextField keyfileTextField = new TextField();
    private final Button selectKeyfileButton = new Button("Select");
    private final Button generateKeyfileButton = new Button("Generate");
    private final HBox keyfileHBox = new HBox(5.0, keyfileLabel, keyfileTextField, selectKeyfileButton, generateKeyfileButton);
    private final Label serialLabel = new Label("Serial");
    private final TextField serialTextField = new TextField();
    private final HBox serialHBox = new HBox(5.0, serialLabel, serialTextField);
    private final Label keyOrSerialRequiredLabel = new Label("Either a Key File or Serial Number must be provided.");
    private final VBox keySerialVBox = new VBox(5.0, keyfileHBox, serialHBox, keyOrSerialRequiredLabel);
    private final Button decryptButton = new Button("Decrypt");
    private final CheckBox verboseCheckbox = new CheckBox("Verbose");
    private final HBox decryptVerboseHBox = new HBox(5.0, decryptButton, verboseCheckbox);
    private final Button clearLogsButton = new Button("Clear Logs");
    private final HBox decryptVerboseClearLogsHBox = new HBox(5.0, decryptVerboseHBox, new Spacer(), clearLogsButton);
    private final TextArea consoleOutputTextArea = new TextArea();

    private final VBox consoleDecryptVBox = new VBox(5.0, consoleOutputTextArea, decryptVerboseClearLogsHBox);
    private final TextAreaOutputStream taOutputStream = new TextAreaOutputStream(consoleOutputTextArea);
    private final PrintStream printStream = new PrintStream(taOutputStream, true);
    private final Button saveSettingsButton = new Button("Save Settings");
    private final Button loadSettingsButton = new Button("Load Settings");
    private final Button resetSettingsButton = new Button("Reset");
    private final HBox settingsHBox = new HBox(5.0, saveSettingsButton, loadSettingsButton, resetSettingsButton);
    private final VBox rootVBox = new VBox(20.0, inputOutputVBox, keySerialVBox, settingsHBox, consoleDecryptVBox);
    private final Rectangle semiTransparentOverlay = new Rectangle();
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    final StackPane rootStackPane = new StackPane(rootVBox, semiTransparentOverlay, progressIndicator);

    private final BooleanProperty isDecrypting = new SimpleBooleanProperty(false);
    private final BooleanProperty isGeneratingKeyfile = new SimpleBooleanProperty(false);

    private final BooleanBinding isDecryptingOrGeneratingKeyfile = isDecrypting.or(isGeneratingKeyfile);

    @SuppressWarnings("unused")
    private final BooleanBinding rootVBoxDisabled = isDecryptingOrGeneratingKeyfile;

    private final BooleanBinding showProgress = isDecryptingOrGeneratingKeyfile;
    private final BooleanBinding ebookfileTextFieldEmpty = ebookfileTextField.textProperty().isEmpty();
    private final BooleanBinding deriveOutputdirDisabled = ebookfileTextFieldEmpty;
    private final BooleanBinding outputdirTextFieldEmpty = outputdirTextField.textProperty().isEmpty();
    private final BooleanBinding ebookfileAndOutputdirEmpty = ebookfileTextFieldEmpty.and(outputdirTextFieldEmpty);
    private final BooleanBinding ebookfileOrOutputdirEmpty = ebookfileTextFieldEmpty.or(outputdirTextFieldEmpty);
    private final BooleanBinding keyfileTextFieldEmpty = keyfileTextField.textProperty().isEmpty();
    private final BooleanBinding serialTextFieldEmpty = serialTextField.textProperty().isEmpty();
    private final BooleanBinding keyAndSerialEmpty = keyfileTextFieldEmpty.and(serialTextFieldEmpty);
    private final BooleanBinding allFieldsEmpty = ebookfileAndOutputdirEmpty.and(keyAndSerialEmpty);
    private final BooleanBinding saveResetDisabled = allFieldsEmpty;
    private final BooleanBinding decryptDisabled = ebookfileOrOutputdirEmpty.or(keyAndSerialEmpty);
    private final BooleanBinding keyOrSerialRequiredVisible = keyAndSerialEmpty;

    @SuppressWarnings("unused")
    private final BooleanBinding keyOrSerialEmpty = keyfileTextFieldEmpty.or(serialTextFieldEmpty);

    private final BooleanBinding consoleOutputTextAreaEmpty = consoleOutputTextArea.textProperty().isEmpty();
    private final BooleanBinding clearLogsDisabled = consoleOutputTextAreaEmpty;

    KindleDeDRMController() {
        configureNodes();
        configurePrintStream();
    }

    private void configureNodes() {
        configureEbookfileTextField();
        configureSelectEbookfileButton();
        configureEbookfileHBox();
        configureOutputdirTextField();
        configureSelectOutputdirButton();
        configureDeriveOutputdirButton();
        configureOutputdirHBox();
        configureInputOutputVBox();
        configureKeyfileTextField();
        configureSelectKeyfileButton();
        configureGenerateKeyfileButton();
        configureKeyfileHBox();
        configureSerialTextField();
        configureSerialHBox();
        configureKeyOrSerialRequiredLabel();
        configureKeySerialVBox();
        configureSaveSettingsButton();
        configureLoadSettingsButton();
        configureResetSettingsButton();
        configureSettingsHBox();
        configureDecryptButton();
        configureConsoleOutputTextArea();
        configureVerboseCheckbox();
        configureClearLogsButton();
        configureDecryptVerboseHBox();
        configureDecryptVerboseClearLogsHBox();
        configureConsoleDecryptVBox();
        configureSemiTransparentOverlay();
        configureProgressIndicator();
        configureRootVBox();
        configureRootStackPane();
    }

    private void configureRootVBox() {
        rootVBox.setPadding(new Insets(10.0));
        rootVBox.disableProperty().bind(showProgress);
    }

    private void configureRootStackPane() {
    }

    private void configureProgressIndicator() {
        progressIndicator.visibleProperty().bind(showProgress);
        progressIndicator.managedProperty().bind(showProgress); // Ensures it doesn't take up space when hidden
    }

    private void configureSemiTransparentOverlay() {
        semiTransparentOverlay.setFill(Color.rgb(0, 0, 0, 0.5)); // Semi-transparent black
        semiTransparentOverlay.widthProperty().bind(rootStackPane.widthProperty());
        semiTransparentOverlay.heightProperty().bind(rootStackPane.heightProperty());
        semiTransparentOverlay.visibleProperty().bind(showProgress);
        semiTransparentOverlay.managedProperty().bind(showProgress);
    }

    private void configurePrintStream() {
        System.setOut(printStream);
        System.setErr(printStream);
    }

    private void configureConsoleDecryptVBox() {
        VBox.setVgrow(consoleDecryptVBox, Priority.ALWAYS);
    }

    private void configureDecryptVerboseClearLogsHBox() {
        decryptVerboseClearLogsHBox.setAlignment(Pos.CENTER);
    }

    private void configureDecryptVerboseHBox() {
        decryptVerboseHBox.setAlignment(Pos.CENTER);
    }

    private void configureClearLogsButton() {
        clearLogsButton.setOnAction(_ -> clearLogs());
        clearLogsButton.disableProperty().bind(clearLogsDisabled);
    }

    private void configureVerboseCheckbox() {
        verboseCheckbox.setOnAction(_ -> Debug.setEnabled(verboseCheckbox.isSelected()));
    }

    private void configureConsoleOutputTextArea() {
        consoleOutputTextArea.setEditable(false);
        consoleOutputTextArea.setWrapText(true);
        consoleOutputTextArea.setPromptText("Console Output");
        consoleOutputTextArea.setFont(Util.consoleOutputFont());

        VBox.setVgrow(consoleOutputTextArea, Priority.ALWAYS);
    }

    private void configureDecryptButton() {
        decryptButton.setOnAction(_ -> decryptBookThrowing());
        decryptButton.disableProperty().bind(decryptDisabled);
    }

    private void configureSettingsHBox() {
        settingsHBox.setAlignment(Pos.CENTER);
    }

    private void configureResetSettingsButton() {
        resetSettingsButton.setOnAction(_ -> resetSettings());
        resetSettingsButton.disableProperty().bind(saveResetDisabled);
    }

    private void configureLoadSettingsButton() {
        loadSettingsButton.setOnAction(_ -> loadSettings());
    }

    private void configureSaveSettingsButton() {
        saveSettingsButton.setOnAction(_ -> saveSettings());
        saveSettingsButton.disableProperty().bind(saveResetDisabled);
    }

    private void configureKeySerialVBox() {
        keySerialVBox.setAlignment(Pos.CENTER);
        keySerialVBox.setPadding(new Insets(5.0));
        keySerialVBox.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5.0), new BorderWidths(1.0))));
    }

    private void configureKeyOrSerialRequiredLabel() {
        keyOrSerialRequiredLabel.setTextAlignment(TextAlignment.CENTER);
        keyOrSerialRequiredLabel.visibleProperty().bind(keyOrSerialRequiredVisible);
        keyOrSerialRequiredLabel.managedProperty().bind(keyOrSerialRequiredVisible);
        keyOrSerialRequiredLabel.setFont(Font.font(12.0));
    }

    private void configureSerialHBox() {
        serialHBox.setAlignment(Pos.CENTER);
    }

    private void configureSerialTextField() {
        serialTextField.setPromptText("Kindle Serial Number");

        HBox.setHgrow(serialTextField, Priority.ALWAYS);
    }

    private void configureKeyfileHBox() {
        keyfileHBox.setAlignment(Pos.CENTER);
    }

    private void configureGenerateKeyfileButton() {
        Tooltip tooltip = new Tooltip("Attempt to generate a key file based on the present installation of Kindle for PC/Mac.");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);

        generateKeyfileButton.setTooltip(tooltip);
        generateKeyfileButton.setOnAction(_ -> generateKeyfileThrowing());
    }

    private void configureSelectKeyfileButton() {
        selectKeyfileButton.setOnAction(_ -> selectKeyfile());
    }

    private void configureKeyfileTextField() {
        keyfileTextField.setPromptText(".k4i File");

        HBox.setHgrow(keyfileTextField, Priority.ALWAYS);
    }

    private void configureInputOutputVBox() {
        inputOutputVBox.setAlignment(Pos.CENTER);
        inputOutputVBox.setPadding(new Insets(5.0));
        inputOutputVBox.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5.0), new BorderWidths(1.0))));
    }

    private void configureOutputdirHBox() {
        outputdirHBox.setAlignment(Pos.CENTER);
    }

    private void configureDeriveOutputdirButton() {
        deriveOutputdirButton.setOnAction(_ -> deriveOutputdir());
        deriveOutputdirButton.disableProperty().bind(deriveOutputdirDisabled);
    }

    private void configureSelectOutputdirButton() {
        selectOutputdirButton.setOnAction(_ -> selectOutputdir());
    }

    private void configureOutputdirTextField() {
        outputdirTextField.setPromptText("Output Directory");

        HBox.setHgrow(outputdirTextField, Priority.ALWAYS);
    }

    private void configureEbookfileHBox() {
        ebookfileHBox.setAlignment(Pos.CENTER);
    }

    private void configureSelectEbookfileButton() {
        selectEbookfileButton.setOnAction(_ -> selectEbookfile());
    }

    private void configureEbookfileTextField() {
        ebookfileTextField.setPromptText("eBook File");

        HBox.setHgrow(ebookfileTextField, Priority.ALWAYS);
    }

    StackPane getRootStackPane() {
        return rootStackPane;
    }

    private void selectEbookfile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("eBook Files", "*.azw", "*.azw3", "*.azw4", "*.mobi", "*.kfx", "*.kfx-zip", "*.zip");
        fileChooser.getExtensionFilters().add(filter);

        if (!ebookfileTextField.getText().isEmpty())
            fileChooser.setInitialDirectory(calculateInitialDirectory(ebookfileTextField.getText()));
        else if (!outputdirTextField.getText().isEmpty())
            fileChooser.setInitialDirectory(calculateInitialDirectory(outputdirTextField.getText()));

        fileChooser.setTitle("Open eBook File");

        File file = fileChooser.showOpenDialog(selectEbookfileButton.getScene().getWindow());

        if (file != null) {
            ebookfileTextField.setText(file.toString());
            if (outputdirTextField.getText().isEmpty()) outputdirTextField.setText(file.getParent());
        }
    }


    private void selectOutputdir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        if (outputdirTextField.getText().isEmpty() && !ebookfileTextField.getText().isEmpty())
            directoryChooser.setInitialDirectory(calculateInitialDirectory(ebookfileTextField.getText()));
        else if (!outputdirTextField.getText().isEmpty())
            directoryChooser.setInitialDirectory(calculateInitialDirectory(outputdirTextField.getText()));

        directoryChooser.setTitle("Select Output Directory");

        File outputDir = directoryChooser.showDialog(selectOutputdirButton.getScene().getWindow());

        if (outputDir != null) outputdirTextField.setText(outputDir.toString());
    }

    private void deriveOutputdir() {
        File file = new File(ebookfileTextField.getText());

        if (file.isFile()) outputdirTextField.setText(file.getParent());
    }


    private void selectKeyfile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        if (!keyfileTextField.getText().isEmpty())
            fileChooser.setInitialDirectory(calculateInitialDirectory(keyfileTextField.getText()));

        fileChooser.setTitle("Select Key File");

        File keyFile = fileChooser.showOpenDialog(selectKeyfileButton.getScene().getWindow());

        if (keyFile != null) keyfileTextField.setText(keyFile.toString());
    }

    @SuppressWarnings("unused")
    private void generateKeyfile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Generate Key File");

        File keyFile = fileChooser.showSaveDialog(generateKeyfileButton.getScene().getWindow());

        if (keyFile != null) {
            if (DeDRM.generateKeyfile(keyFile.toString())) {
                keyfileTextField.setText(keyFile.toString());
                System.out.printf("Generated key file: %s%n", keyFile);
            } else System.err.println("Error generating key file");
        }
    }

    private void generateKeyfileThrowing() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Generate Key File");

        File keyFile = fileChooser.showSaveDialog(generateKeyfileButton.getScene().getWindow());

        if (keyFile != null) {
            Runnable task = () -> {
                try {
                    Platform.runLater(() -> isGeneratingKeyfile.set(true));
                    DeDRM.generateKeyfileThrowing(keyFile.toString());
                    Platform.runLater(() -> keyfileTextField.setText(keyFile.toString()));
                    System.out.printf("Generated key file: %s%n", keyFile);
                } catch (Exception e) {
                    System.err.printf("Error generating key file: %s%n", e.getMessage());
                } finally {
                    Platform.runLater(() -> isGeneratingKeyfile.set(false));
                }
            };

            @SuppressWarnings("resource")
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task);
            executor.shutdown();
        }
    }

    private void saveSettings() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        SettingsDict settings = new SettingsDict(ebookfileTextField.getText(), outputdirTextField.getText(), keyfileTextField.getText(), serialTextField.getText());

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".json Files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        fileChooser.setTitle("Save Settings File");

        File settingsFile = fileChooser.showSaveDialog(saveSettingsButton.getScene().getWindow());

        if (settingsFile != null) {
            try {
                settings.writeToFile(settingsFile);
            } catch (IOException e) {
                System.err.printf("Error saving settings file: %s%n", e.getMessage());
                return;
            }

            System.out.printf("Saved settings to: %s%n", settingsFile);
        }
    }

    private void loadSettings() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".json Files", "*.json");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Select Settings File");

        File settingsFile = fileChooser.showOpenDialog(loadSettingsButton.getScene().getWindow());

        if (settingsFile != null) {
            try {
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
                SettingsDict settings = new SettingsDict(settingsFile);

                ebookfileTextField.setText(settings.getInputFile());
                outputdirTextField.setText(settings.getOutputFile());
                keyfileTextField.setText(settings.getKeyFile());
                serialTextField.setText(settings.getSerial());
            } catch (IOException e) {
                System.err.printf("Error loading settings file: %s%n", e.getMessage());
                return;
            }

            System.out.printf("Loaded settings from: %s%n", settingsFile);
        }
    }

    private void resetSettings() {
        ebookfileTextField.clear();
        outputdirTextField.clear();
        keyfileTextField.clear();
        serialTextField.clear();
    }

    @SuppressWarnings("unused")
    private void decryptBook() {
        String infile = ebookfileTextField.getText();
        String outdir = outputdirTextField.getText();
        String keyfile = keyfileTextField.getText();
        String serial = serialTextField.getText();

        Debug.printf("Input File: %s%n", infile);
        Debug.printf("Output Directory: %s%n", outdir);
        Debug.printf("Key File: %s%n", keyfile);
        Debug.printf("Serial: %s%n", serial);

        DeDRM.decryptBookWithKDatabaseAndSerial(infile, outdir, keyfile, serial);
    }

    private void decryptBookThrowing() {
        String infile = ebookfileTextField.getText();
        String outdir = outputdirTextField.getText();
        String keyfile = keyfileTextField.getText();
        String serial = serialTextField.getText();

        Debug.printf("Input File: %s%n", infile);
        Debug.printf("Output Directory: %s%n", outdir);
        Debug.printf("Key File: %s%n", keyfile);
        Debug.printf("Serial: %s%n", serial);

        Runnable task = () -> {
            Platform.runLater(() -> isDecrypting.set(true));

            try {
                DeDRM.decryptBookWithKDatabaseAndSerialThrowing(infile, outdir, keyfile, serial);
            } catch (Exception e) {
                System.err.printf("Error decrypting book: %s%n", e.getMessage());
            } finally {
                Platform.runLater(() -> isDecrypting.set(false));
            }
        };

        @SuppressWarnings("resource")
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(task);
        executor.shutdown();
    }

    private void clearLogs() {
        consoleOutputTextArea.clear();
    }

    private File calculateInitialDirectory(String text) {
        if (text == null || text.isEmpty()) return null;

        File file = new File(text);

        if (file.isDirectory()) return file;
        else if (file.isFile()) return file.getParentFile();
        else return null;
    }
}
