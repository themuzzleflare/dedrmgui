/*
 * Copyright © 2024-2025 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.DeDRM;
import cloud.tavitian.dedrmtools.Debug;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class KindleDeDRMBaseController implements Closeable, Initializable {
    protected final BooleanProperty isDecrypting = new SimpleBooleanProperty(false);
    protected final BooleanProperty isGeneratingKeyfile = new SimpleBooleanProperty(false);
    protected final BooleanBinding isDecryptingOrGeneratingKeyfile = isDecrypting.or(isGeneratingKeyfile);
    @SuppressWarnings("unused")
    protected final BooleanBinding rootVBoxDisabled = isDecryptingOrGeneratingKeyfile;
    protected final BooleanBinding showProgress = isDecryptingOrGeneratingKeyfile;

    @FXML
    protected Label ebookfileLabel;
    @FXML
    protected TextField ebookfileTextField;
    @FXML
    protected Button selectEbookfileButton;
    @FXML
    protected HBox ebookfileHBox;
    @FXML
    protected Label outputdirLabel;
    @FXML
    protected TextField outputdirTextField;
    @FXML
    protected Button selectOutputdirButton;
    @FXML
    protected Button deriveOutputdirButton;
    @FXML
    protected HBox outputdirHBox;
    @FXML
    protected VBox inputOutputVBox;
    @FXML
    protected Label keyfileLabel;
    @FXML
    protected TextField keyfileTextField;
    @FXML
    protected Button selectKeyfileButton;
    @FXML
    protected Button generateKeyfileButton;
    @FXML
    protected HBox keyfileHBox;
    @FXML
    protected Label serialLabel;
    @FXML
    protected TextField serialTextField;
    @FXML
    protected HBox serialHBox;
    @FXML
    protected Label keyOrSerialRequiredLabel;
    @FXML
    protected VBox keySerialVBox;
    @FXML
    protected Button decryptButton;
    @FXML
    protected CheckBox verboseCheckbox;
    @FXML
    protected HBox decryptVerboseHBox;
    @FXML
    protected Button clearLogsButton;
    @FXML
    protected TextArea consoleOutputTextArea;
    @FXML
    protected HBox decryptVerboseClearLogsHBox;
    @FXML
    protected VBox consoleDecryptVBox;
    @FXML
    protected Button saveSettingsButton;
    @FXML
    protected Button loadSettingsButton;
    @FXML
    protected Button resetSettingsButton;
    @FXML
    protected HBox settingsHBox;
    @FXML
    protected MenuItem decryptMenuItem;
    @FXML
    protected MenuItem generateKeyfileMenuItem;
    @FXML
    protected MenuItem clearLogsMenuItem;
    @FXML
    protected MenuItem saveSettingsMenuItem;
    @FXML
    protected MenuItem loadSettingsMenuItem;
    @FXML
    protected MenuItem resetSettingsMenuItem;
    @FXML
    protected Menu runMenu;
    @FXML
    protected Menu settingsMenu;
    @FXML
    protected MenuBar menuBar;
    @FXML
    protected VBox rootVBox;
    @FXML
    protected VBox rootVBoxWithMenuBar;
    @FXML
    protected Rectangle semiTransparentOverlay;
    @FXML
    protected ProgressIndicator progressIndicator;
    @FXML
    protected StackPane rootStackPane;

    protected OutputStream taOutputStream;
    protected PrintStream printStream;

    protected BooleanBinding ebookfileTextFieldEmpty;
    protected BooleanBinding deriveOutputdirDisabled;
    protected BooleanBinding outputdirTextFieldEmpty;
    protected BooleanBinding ebookfileAndOutputdirEmpty;
    protected BooleanBinding ebookfileOrOutputdirEmpty;
    protected BooleanBinding keyfileTextFieldEmpty;
    protected BooleanBinding serialTextFieldEmpty;
    protected BooleanBinding keyAndSerialEmpty;
    protected BooleanBinding allFieldsEmpty;
    protected BooleanBinding saveResetDisabled;
    protected BooleanBinding decryptDisabled;
    protected BooleanBinding keyOrSerialRequiredVisible;
    protected BooleanBinding keyOrSerialEmpty;
    protected BooleanBinding consoleOutputTextAreaEmpty;
    protected BooleanBinding clearLogsDisabled;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureBooleanBindings();
        configureNodes();
        configureStandardOutput();
    }

    protected void configureNodes() {
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
        configureDecryptMenuItem();
        configureGenerateKeyfileMenuItem();
        configureClearLogsMenuItem();
        configureSaveSettingsMenuItem();
        configureLoadSettingsMenuItem();
        configureResetSettingsMenuItem();
        configureMenuBar();
    }

    protected void configureEbookfileTextField() {
    }

    protected void configureSelectEbookfileButton() {
    }

    protected void configureEbookfileHBox() {
    }

    protected void configureOutputdirTextField() {
    }

    protected void configureSelectOutputdirButton() {
    }

    protected void configureDeriveOutputdirButton() {
    }

    protected void configureOutputdirHBox() {
    }

    protected void configureInputOutputVBox() {
    }

    protected void configureKeyfileTextField() {
    }

    protected void configureSelectKeyfileButton() {
    }

    protected void configureGenerateKeyfileButton() {
    }

    protected void configureKeyfileHBox() {
    }

    protected void configureSerialTextField() {
    }

    protected void configureSerialHBox() {
    }

    protected void configureKeyOrSerialRequiredLabel() {
    }

    protected void configureKeySerialVBox() {
    }

    protected void configureSaveSettingsButton() {
    }

    protected void configureLoadSettingsButton() {
    }

    protected void configureResetSettingsButton() {
    }

    protected void configureSettingsHBox() {
    }

    protected void configureDecryptButton() {
    }

    protected void configureConsoleOutputTextArea() {
    }

    protected void configureVerboseCheckbox() {
    }

    protected void configureClearLogsButton() {
    }

    protected void configureDecryptVerboseHBox() {
    }

    protected void configureDecryptVerboseClearLogsHBox() {
    }

    protected void configureConsoleDecryptVBox() {
    }

    protected void configureSemiTransparentOverlay() {
    }

    protected void configureProgressIndicator() {
    }

    protected void configureRootVBox() {
    }

    protected void configureDecryptMenuItem() {
    }

    protected void configureGenerateKeyfileMenuItem() {
    }

    protected void configureClearLogsMenuItem() {
    }

    protected void configureSaveSettingsMenuItem() {
    }

    protected void configureLoadSettingsMenuItem() {
    }

    protected void configureResetSettingsMenuItem() {
    }

    protected void configureMenuBar() {
    }

    protected void configureBooleanBindings() {
        ebookfileTextFieldEmpty = ebookfileTextField.textProperty().isEmpty();
        deriveOutputdirDisabled = ebookfileTextFieldEmpty;
        outputdirTextFieldEmpty = outputdirTextField.textProperty().isEmpty();
        ebookfileAndOutputdirEmpty = ebookfileTextFieldEmpty.and(outputdirTextFieldEmpty);
        ebookfileOrOutputdirEmpty = ebookfileTextFieldEmpty.or(outputdirTextFieldEmpty);
        keyfileTextFieldEmpty = keyfileTextField.textProperty().isEmpty();
        serialTextFieldEmpty = serialTextField.textProperty().isEmpty();
        keyAndSerialEmpty = keyfileTextFieldEmpty.and(serialTextFieldEmpty);
        allFieldsEmpty = ebookfileAndOutputdirEmpty.and(keyAndSerialEmpty);
        saveResetDisabled = allFieldsEmpty;
        decryptDisabled = ebookfileOrOutputdirEmpty.or(keyAndSerialEmpty);
        keyOrSerialRequiredVisible = keyAndSerialEmpty;
        keyOrSerialEmpty = keyfileTextFieldEmpty.or(serialTextFieldEmpty);
        consoleOutputTextAreaEmpty = consoleOutputTextArea.textProperty().isEmpty();
        clearLogsDisabled = consoleOutputTextAreaEmpty;
    }

    protected void configureStandardOutput() {
        System.setOut(printStream);
        System.setErr(printStream);
    }

    @FXML
    protected void selectEbookfile() {
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

    @FXML
    protected void selectOutputdir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        if (outputdirTextField.getText().isEmpty() && !ebookfileTextField.getText().isEmpty())
            directoryChooser.setInitialDirectory(calculateInitialDirectory(ebookfileTextField.getText()));
        else if (!outputdirTextField.getText().isEmpty())
            directoryChooser.setInitialDirectory(calculateInitialDirectory(outputdirTextField.getText()));

        directoryChooser.setTitle("Select Output Directory");

        File outputDir = directoryChooser.showDialog(selectOutputdirButton.getScene().getWindow());

        if (outputDir != null) outputdirTextField.setText(outputDir.toString());
    }

    @FXML
    protected void deriveOutputdir() {
        File file = new File(ebookfileTextField.getText());

        if (file.isFile()) outputdirTextField.setText(file.getParent());
    }

    @FXML
    protected void selectKeyfile() {
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
    protected void generateKeyfile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialFileName("kindle");
        fileChooser.setTitle("Generate Key File");

        File keyFile = fileChooser.showSaveDialog(generateKeyfileButton.getScene().getWindow());

        if (keyFile != null) {
            if (DeDRM.generateKeyfile(keyFile.toString())) {
                keyfileTextField.setText(keyFile.toString());
                System.out.printf("Generated key file: %s%n", keyFile);
            } else System.err.println("Error generating key file");
        }
    }

    @FXML
    protected void generateKeyfileThrowing() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".k4i Files", "*.k4i");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialFileName("kindle");
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

    @FXML
    protected void saveSettings() {
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

    @FXML
    protected void loadSettings() {
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

    @FXML
    protected void resetSettings() {
        ebookfileTextField.clear();
        outputdirTextField.clear();
        keyfileTextField.clear();
        serialTextField.clear();
    }

    @SuppressWarnings("unused")
    protected void decryptBook() {
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

    @FXML
    protected void decryptBookThrowing() {
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

    @FXML
    protected void clearLogs() {
        consoleOutputTextArea.clear();
    }

    protected File calculateInitialDirectory(String text) {
        if (text == null || text.isEmpty()) return null;

        File file = new File(text);

        if (file.isDirectory()) return file;
        else if (file.isFile()) return file.getParentFile();
        else return null;
    }

    @Override
    public void close() throws IOException {
        if (taOutputStream != null) taOutputStream.close();
        if (printStream != null) printStream.close();
    }
}
