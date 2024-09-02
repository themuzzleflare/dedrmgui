/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.DeDRM;
import cloud.tavitian.dedrmtools.Debug;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

final class KindleDeDRMController extends VBox {
    private final TextAreaPrintStream printStream;

    private final HBox decryptDebugHbox;
    private final CheckBox debugCheckBox;
    private final HBox ebookFileHbox;
    private final Label ebookFileLabel;
    private final TextField ebookFileTextField;
    private final Button ebookFileButton;
    private final HBox outputDirHbox;
    private final Label outputDirLabel;
    private final TextField outputDirTextField;
    private final Button outputDirButton;
    private final HBox keyFileHbox;
    private final Label keyFileLabel;
    private final TextField keyFileTextField;
    private final Button keyFileButton;
    private final HBox serialHbox;
    private final Label serialLabel;
    private final TextField serialTextField;
    private final Button decryptButton;
    private final TextArea textArea;
    private final HBox settingsHbox;
    private final Button saveSettingsButton;
    private final Button loadSettingsButton;
    private final VBox inputOutputVbox;
    private final VBox keySerialVbox;
    private final Button clearLogsButton;
    private final VBox logDecryptVbox;
    private final HBox decryptHbox;
    private final Button resetButton;
    private final Button generateK4iButton;
    private final Label keyOrSerialRequiredLabel;
    private final Button deriveOutputDirButton;

    public KindleDeDRMController() {
        ebookFileLabel = new Label("eBook File:");

        ebookFileTextField = new TextField();
        ebookFileTextField.setPromptText("eBook File");

        ebookFileButton = new Button("Select");
        ebookFileButton.setOnAction(_ -> selectEbookFile());

        ebookFileHbox = new HBox(5.0, ebookFileLabel, ebookFileTextField, ebookFileButton);
        ebookFileHbox.setAlignment(Pos.CENTER);

        outputDirLabel = new Label("Output Directory:");

        outputDirTextField = new TextField();
        outputDirTextField.setPromptText("Output Directory");

        outputDirButton = new Button("Select");
        outputDirButton.setOnAction(_ -> selectOutputDir());

        deriveOutputDirButton = new Button("Derive");
        deriveOutputDirButton.setOnAction(_ -> deriveOutputDir());

        deriveOutputDirButton.disableProperty().bind(ebookFileTextField.textProperty().isEmpty());

        outputDirHbox = new HBox(5.0, outputDirLabel, outputDirTextField, outputDirButton, deriveOutputDirButton);
        outputDirHbox.setAlignment(Pos.CENTER);

        inputOutputVbox = new VBox(5.0, ebookFileHbox, outputDirHbox);
        inputOutputVbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5.0), new Insets(-5.0))));

        keyFileLabel = new Label("Key File:");

        keyFileTextField = new TextField();
        keyFileTextField.setPromptText(".k4i File");

        keyFileButton = new Button("Select");
        keyFileButton.setOnAction(_ -> selectKeyFile());

        generateK4iButton = new Button("Generate");

        Tooltip tooltip = new Tooltip("Attempt to generate a key file based on the present installation of Kindle for PC/Mac.");
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);

        generateK4iButton.setTooltip(tooltip);
        generateK4iButton.setOnAction(_ -> generateKeyFile());

        keyFileHbox = new HBox(5.0, keyFileLabel, keyFileTextField, keyFileButton, generateK4iButton);
        keyFileHbox.setAlignment(Pos.CENTER);

        serialLabel = new Label("Serial:");

        serialTextField = new TextField();
        serialTextField.setPromptText("Kindle Serial Number");

        serialHbox = new HBox(5.0, serialLabel, serialTextField);
        serialHbox.setAlignment(Pos.CENTER);

        keyOrSerialRequiredLabel = new Label("Either a Key File or Serial Number must be provided.");
        keyOrSerialRequiredLabel.setTextAlignment(TextAlignment.CENTER);
        keyOrSerialRequiredLabel.visibleProperty().bind(
                keyFileTextField.textProperty().isEmpty().and(
                        serialTextField.textProperty().isEmpty()
                )
        );

        keySerialVbox = new VBox(5.0, keyFileHbox, serialHbox, keyOrSerialRequiredLabel);
        keySerialVbox.setAlignment(Pos.CENTER);
        keySerialVbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5.0), new Insets(-5.0))));

        saveSettingsButton = new Button("Save Settings");
        saveSettingsButton.setOnAction(_ -> saveSettings());

        saveSettingsButton.disableProperty().bind(
                ebookFileTextField.textProperty().isEmpty().and(
                        outputDirTextField.textProperty().isEmpty().and(
                                keyFileTextField.textProperty().isEmpty().and(
                                        serialTextField.textProperty().isEmpty()
                                )
                        )
                )
        );

        loadSettingsButton = new Button("Load Settings");
        loadSettingsButton.setOnAction(_ -> loadSettings());

        resetButton = new Button("Reset");
        resetButton.setOnAction(_ -> resetSettings());

        resetButton.disableProperty().bind(
                ebookFileTextField.textProperty().isEmpty().and(
                        outputDirTextField.textProperty().isEmpty().and(
                                keyFileTextField.textProperty().isEmpty().and(
                                        serialTextField.textProperty().isEmpty()
                                )
                        )
                )
        );

        settingsHbox = new HBox(5.0, saveSettingsButton, loadSettingsButton, resetButton);
        settingsHbox.setAlignment(Pos.CENTER);

        decryptButton = new Button("Decrypt");
        decryptButton.setOnAction(_ -> decryptBook());

        decryptButton.disableProperty().bind(
                ebookFileTextField.textProperty().isEmpty().or(
                        outputDirTextField.textProperty().isEmpty().or(
                                keyFileTextField.textProperty().isEmpty().and(
                                        serialTextField.textProperty().isEmpty()
                                )
                        )
                )
        );

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        debugCheckBox = new CheckBox("Verbose");
        debugCheckBox.setOnAction(_ -> Debug.setEnabled(debugCheckBox.isSelected()));

        clearLogsButton = new Button("Clear Logs");
        clearLogsButton.setOnAction(_ -> clearLogs());

        clearLogsButton.disableProperty().bind(textArea.textProperty().isEmpty());

        decryptDebugHbox = new HBox(5.0, decryptButton, debugCheckBox);
        decryptDebugHbox.setAlignment(Pos.CENTER);

        HBox.setHgrow(decryptDebugHbox, Priority.ALWAYS);

        decryptHbox = new HBox(5.0, decryptDebugHbox, clearLogsButton);
        decryptHbox.setAlignment(Pos.CENTER);

        logDecryptVbox = new VBox(5.0, textArea, decryptHbox);

        setSpacing(20.0);
        setPadding(new Insets(10.0));

        getChildren().addAll(inputOutputVbox, keySerialVbox, settingsHbox, logDecryptVbox);

        printStream = new TextAreaPrintStream(textArea);

        System.setOut(printStream);
        System.setErr(printStream);
    }


    private void selectEbookFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("eBook Files", "*.azw", "*.azw3", "*.mobi", "*.kfx", "*.kfx-zip", "*.zip");
        fileChooser.getExtensionFilters().add(filter);

        if (!ebookFileTextField.getText().isEmpty())
            fileChooser.setInitialDirectory(calculateInitialDirectory(ebookFileTextField.getText()));
        else if (!outputDirTextField.getText().isEmpty())
            fileChooser.setInitialDirectory(calculateInitialDirectory(outputDirTextField.getText()));

        fileChooser.setTitle("Open eBook File");

        File file = fileChooser.showOpenDialog(ebookFileButton.getScene().getWindow());

        if (file != null) {
            ebookFileTextField.setText(file.toString());
            if (outputDirTextField.getText().isEmpty()) outputDirTextField.setText(file.getParent());
        }
    }


    private void selectOutputDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        if (outputDirTextField.getText().isEmpty() && !ebookFileTextField.getText().isEmpty())
            directoryChooser.setInitialDirectory(calculateInitialDirectory(ebookFileTextField.getText()));
        else if (!outputDirTextField.getText().isEmpty())
            directoryChooser.setInitialDirectory(calculateInitialDirectory(outputDirTextField.getText()));

        directoryChooser.setTitle("Select Output Directory");

        File outputDir = directoryChooser.showDialog(outputDirButton.getScene().getWindow());

        if (outputDir != null) outputDirTextField.setText(outputDir.toString());
    }

    private void deriveOutputDir() {
        File file = new File(ebookFileTextField.getText());

        if (file.isFile()) outputDirTextField.setText(file.getParent());
    }


    private void selectKeyFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        if (!keyFileTextField.getText().isEmpty())
            fileChooser.setInitialDirectory(calculateInitialDirectory(keyFileTextField.getText()));

        fileChooser.setTitle("Select Key File");

        File keyFile = fileChooser.showOpenDialog(keyFileButton.getScene().getWindow());

        if (keyFile != null) keyFileTextField.setText(keyFile.toString());
    }

    private void generateKeyFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Generate Key File");

        File keyFile = fileChooser.showSaveDialog(generateK4iButton.getScene().getWindow());

        if (keyFile != null) {
            if (DeDRM.generateKeyfile(keyFile.toString())) {
                keyFileTextField.setText(keyFile.toString());
                System.out.printf("Generated key file: %s%n", keyFile);
            } else System.err.println("Error generating key file");
        }
    }

    private void saveSettings() {
        SettingsDict settings = new SettingsDict(ebookFileTextField.getText(), outputDirTextField.getText(), keyFileTextField.getText(), serialTextField.getText());

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".json Files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        fileChooser.setTitle("Save Settings File");

        File settingsFile = fileChooser.showSaveDialog(saveSettingsButton.getScene().getWindow());

        if (settingsFile != null) {
            try {
                settings.writeToFile(settingsFile);
            } catch (Exception e) {
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
                SettingsDict settings = new SettingsDict(settingsFile);

                ebookFileTextField.setText(settings.getInputFile());
                outputDirTextField.setText(settings.getOutputFile());
                keyFileTextField.setText(settings.getKeyFile());
                serialTextField.setText(settings.getSerial());
            } catch (Exception e) {
                System.err.printf("Error loading settings file: %s%n", e.getMessage());
                return;
            }

            System.out.printf("Loaded settings from: %s%n", settingsFile);
        }
    }

    private void resetSettings() {
        ebookFileTextField.clear();
        outputDirTextField.clear();
        keyFileTextField.clear();
        serialTextField.clear();
    }

    private void decryptBook() {
        String infile = ebookFileTextField.getText();
        String outdir = outputDirTextField.getText();
        String keyfile = keyFileTextField.getText();
        String serial = serialTextField.getText();

        Debug.printf("Input File: %s%n", infile);
        Debug.printf("Output Directory: %s%n", outdir);
        Debug.printf("Key File: %s%n", keyfile);
        Debug.printf("Serial: %s%n", serial);

        DeDRM.decryptBookWithKDatabaseAndSerial(infile, outdir, keyfile, serial);
    }

    private void clearLogs() {
        textArea.clear();
    }

    private File calculateInitialDirectory(String text) {
        if (text == null || text.isEmpty()) return null;

        File file = new File(text);

        if (file.isDirectory()) return file;
        else if (file.isFile()) return file.getParentFile();
        else return null;
    }
}
