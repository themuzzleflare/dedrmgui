/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.DeDRM;
import cloud.tavitian.dedrmtools.Debug;
import cloud.tavitian.dedrmtools.kindlekeys.KindleKey;
import com.google.gson.Gson;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

final class KindleDeDRMController extends VBox {
    private static final Gson gson = new Gson();

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

        outputDirHbox = new HBox(5.0, outputDirLabel, outputDirTextField, outputDirButton);
        outputDirHbox.setAlignment(Pos.CENTER);

        inputOutputVbox = new VBox(5.0, ebookFileHbox, outputDirHbox);
        inputOutputVbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5.0), new Insets(-5.0))));

        keyFileLabel = new Label("Key File:");

        keyFileTextField = new TextField();
        keyFileTextField.setPromptText(".k4i File");

        keyFileButton = new Button("Select");
        keyFileButton.setOnAction(_ -> selectKeyFile());

        generateK4iButton = new Button("Generate");
        generateK4iButton.setOnAction(_ -> generateKeyFile());

        keyFileHbox = new HBox(5.0, keyFileLabel, keyFileTextField, keyFileButton, generateK4iButton);
        keyFileHbox.setAlignment(Pos.CENTER);

        serialLabel = new Label("Serial:");

        serialTextField = new TextField();
        serialTextField.setPromptText("Kindle Serial Number");

        serialHbox = new HBox(5.0, serialLabel, serialTextField);
        serialHbox.setAlignment(Pos.CENTER);

        keySerialVbox = new VBox(5.0, keyFileHbox, serialHbox);
        keySerialVbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5.0), new Insets(-5.0))));

        saveSettingsButton = new Button("Save Settings");
        saveSettingsButton.setOnAction(_ -> saveSettings());

        loadSettingsButton = new Button("Load Settings");
        loadSettingsButton.setOnAction(_ -> loadSettings());

        resetButton = new Button("Reset");
        resetButton.setOnAction(_ -> resetSettings());

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

        debugCheckBox = new CheckBox("Verbose");
        debugCheckBox.setOnAction(_ -> Debug.setEnabled(debugCheckBox.isSelected()));

        clearLogsButton = new Button("Clear Logs");
        clearLogsButton.setOnAction(_ -> clearLogs());

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
        fileChooser.setTitle("Open eBook File");

        File file = fileChooser.showOpenDialog(ebookFileButton.getScene().getWindow());

        if (file != null) {
            ebookFileTextField.setText(file.toString());
            outputDirTextField.setText(file.getParent());
        }
    }


    private void selectOutputDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        if (!ebookFileTextField.getText().isEmpty())
            directoryChooser.setInitialDirectory(new File(ebookFileTextField.getText()).getParentFile());

        directoryChooser.setTitle("Select Output Directory");

        File outputDir = directoryChooser.showDialog(outputDirButton.getScene().getWindow());

        if (outputDir != null) outputDirTextField.setText(outputDir.toString());
    }


    private void selectKeyFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i", "*.json");
        fileChooser.getExtensionFilters().add(filter);
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
            KindleKey instance = KindleKey.getInstance();

            if (instance.getKey(keyFile.toString())) {
                keyFileTextField.setText(keyFile.toString());
                System.out.printf("Generated key file: %s%n", keyFile);
            } else System.err.println("Error generating key file");
        }
    }

    private void saveSettings() {
        SettingsDict settings = new SettingsDict();
        settings.setInputFile(ebookFileTextField.getText());
        settings.setOutputFile(outputDirTextField.getText());
        settings.setKeyFile(keyFileTextField.getText());
        settings.setSerial(serialTextField.getText());

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".json Files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        fileChooser.setTitle("Save Settings File");

        File settingsFile = fileChooser.showSaveDialog(saveSettingsButton.getScene().getWindow());

        if (settingsFile != null) {
            try (FileWriter writer = new FileWriter(settingsFile)) {
                gson.toJson(settings, writer);
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
            try (FileReader reader = new FileReader(settingsFile)) {
                SettingsDict settings = gson.fromJson(reader, SettingsDict.class);
                ebookFileTextField.setText(settings.getInputFile());
                outputDirTextField.setText(settings.getOutputFile());
                keyFileTextField.setText(settings.getKeyFile());
                serialTextField.setText(settings.getSerial());
            } catch (Exception e) {
                System.err.println("Error loading settings file: " + e.getMessage());
                return;
            }

            System.out.println("Loaded settings from: " + settingsFile);
        }
    }

    private void resetSettings() {
        ebookFileTextField.clear();
        outputDirTextField.clear();
        keyFileTextField.clear();
        serialTextField.clear();
    }

    private void decryptBook() {
        String serial = serialTextField.getText();
        String infile = ebookFileTextField.getText();
        String outdir = outputDirTextField.getText();
        String keyfile = keyFileTextField.getText();

        Debug.println("Serial: " + serial);
        Debug.println("Input File: " + infile);
        Debug.println("Output Directory: " + outdir);
        Debug.println("Key File: " + keyfile);

        DeDRM.decryptBookWithKDatabaseAndSerial(infile, outdir, keyfile, serial);
    }

    private void clearLogs() {
        textArea.clear();
    }
}
