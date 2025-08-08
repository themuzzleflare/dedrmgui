/*
 * Copyright © 2024-2025 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.Debug;
import javafx.fxml.FXML;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public final class KindleDeDRMFXMLController extends KindleDeDRMBaseController {
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        nonNullAssertions();
        super.initialize(location, resources);
    }

    @Override
    protected void configureStandardOutput() {
        configureTextAreaOutputStream();
        configurePrintStream();
        super.configureStandardOutput();
    }

    @Override
    protected void configureDecryptMenuItem() {
        decryptMenuItem.disableProperty().bind(decryptDisabled);
    }

    @Override
    protected void configureGenerateKeyfileMenuItem() {
        generateKeyfileMenuItem.disableProperty().bind(isGeneratingKeyfile);
    }

    @Override
    protected void configureClearLogsMenuItem() {
        clearLogsMenuItem.disableProperty().bind(clearLogsDisabled);
    }

    @Override
    protected void configureSaveSettingsMenuItem() {
        saveSettingsMenuItem.disableProperty().bind(saveResetDisabled);
    }

    @Override
    protected void configureResetSettingsMenuItem() {
        resetSettingsMenuItem.disableProperty().bind(saveResetDisabled);
    }

    @Override
    protected void configureRootVBox() {
        rootVBox.disableProperty().bind(showProgress);
    }

    @Override
    protected void configureProgressIndicator() {
        progressIndicator.visibleProperty().bind(showProgress);
        progressIndicator.managedProperty().bind(showProgress); // Ensures it doesn't take up space when hidden
    }

    @Override
    protected void configureSemiTransparentOverlay() {
        semiTransparentOverlay.visibleProperty().bind(showProgress);
        semiTransparentOverlay.managedProperty().bind(showProgress);
    }

    private void configurePrintStream() {
        printStream = new PrintStream(taOutputStream, true);
    }

    private void configureTextAreaOutputStream() {
        taOutputStream = new BufferedTextAreaOutputStream(consoleOutputTextArea);
    }

    @Override
    protected void configureClearLogsButton() {
        clearLogsButton.disableProperty().bind(clearLogsDisabled);
    }

    @Override
    protected void configureConsoleOutputTextArea() {
        consoleOutputTextArea.setFont(Util.consoleOutputFont());
    }

    @Override
    protected void configureDecryptButton() {
        decryptButton.disableProperty().bind(decryptDisabled);
    }

    @Override
    protected void configureResetSettingsButton() {
        resetSettingsButton.disableProperty().bind(saveResetDisabled);
    }

    @Override
    protected void configureSaveSettingsButton() {
        saveSettingsButton.disableProperty().bind(saveResetDisabled);
    }

    @Override
    protected void configureKeyOrSerialRequiredLabel() {
        keyOrSerialRequiredLabel.visibleProperty().bind(keyOrSerialRequiredVisible);
        keyOrSerialRequiredLabel.managedProperty().bind(keyOrSerialRequiredVisible);
    }

    @Override
    protected void configureDeriveOutputdirButton() {
        deriveOutputdirButton.disableProperty().bind(deriveOutputdirDisabled);
    }

    private void nonNullAssertions() {
        assert clearLogsButton != null : "fx:id=\"clearLogsButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert clearLogsMenuItem != null : "fx:id=\"clearLogsMenuItem\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert consoleDecryptVBox != null : "fx:id=\"consoleDecryptVBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert consoleOutputTextArea != null : "fx:id=\"consoleOutputTextArea\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert decryptButton != null : "fx:id=\"decryptButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert decryptMenuItem != null : "fx:id=\"decryptMenuItem\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert decryptVerboseClearLogsHBox != null : "fx:id=\"decryptVerboseClearLogsHBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert decryptVerboseHBox != null : "fx:id=\"decryptVerboseHBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert deriveOutputdirButton != null : "fx:id=\"deriveOutputdirButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert ebookfileHBox != null : "fx:id=\"ebookfileHBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert ebookfileLabel != null : "fx:id=\"ebookfileLabel\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert ebookfileTextField != null : "fx:id=\"ebookfileTextField\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert generateKeyfileButton != null : "fx:id=\"generateKeyfileButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert generateKeyfileMenuItem != null : "fx:id=\"generateKeyfileMenuItem\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert inputOutputVBox != null : "fx:id=\"inputOutputVBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert keyOrSerialRequiredLabel != null : "fx:id=\"keyOrSerialRequiredLabel\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert keySerialVBox != null : "fx:id=\"keySerialVBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert keyfileHBox != null : "fx:id=\"keyfileHBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert keyfileLabel != null : "fx:id=\"keyfileLabel\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert keyfileTextField != null : "fx:id=\"keyfileTextField\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert loadSettingsButton != null : "fx:id=\"loadSettingsButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert loadSettingsMenuItem != null : "fx:id=\"loadSettingsMenuItem\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert outputdirHBox != null : "fx:id=\"outputdirHBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert outputdirLabel != null : "fx:id=\"outputdirLabel\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert outputdirTextField != null : "fx:id=\"outputdirTextField\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert progressIndicator != null : "fx:id=\"progressIndicator\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert resetSettingsButton != null : "fx:id=\"resetSettingsButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert resetSettingsMenuItem != null : "fx:id=\"resetSettingsMenuItem\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert rootStackPane != null : "fx:id=\"rootStackPane\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert rootVBox != null : "fx:id=\"rootVBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert rootVBoxWithMenuBar != null : "fx:id=\"rootVBoxWithMenuBar\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert runMenu != null : "fx:id=\"runMenu\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert saveSettingsButton != null : "fx:id=\"saveSettingsButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert saveSettingsMenuItem != null : "fx:id=\"saveSettingsMenuItem\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert selectEbookfileButton != null : "fx:id=\"selectEbookfileButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert selectKeyfileButton != null : "fx:id=\"selectKeyfileButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert selectOutputdirButton != null : "fx:id=\"selectOutputdirButton\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert semiTransparentOverlay != null : "fx:id=\"semiTransparentOverlay\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert serialHBox != null : "fx:id=\"serialHBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert serialLabel != null : "fx:id=\"serialLabel\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert serialTextField != null : "fx:id=\"serialTextField\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert settingsHBox != null : "fx:id=\"settingsHBox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert settingsMenu != null : "fx:id=\"settingsMenu\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
        assert verboseCheckbox != null : "fx:id=\"verboseCheckbox\" was not injected: check your FXML file 'kindlededrm-view.fxml'.";
    }

    @FXML
    private void toggleVerbose() {
        Debug.setEnabled(verboseCheckbox.isSelected());
    }
}
