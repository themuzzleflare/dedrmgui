/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.DeDRM;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.nio.file.Path;

public class KindleDeDRMController {
    Path infile;
    Path outdir;
    Path keyfile;
    String serial;
    @FXML
    private HBox ebookFileHbox;
    @FXML
    private Label ebookFileLabel;
    @FXML
    private TextField ebookFileTextField;
    @FXML
    private Button ebookFileButton;
    @FXML
    private HBox outputDirHbox;
    @FXML
    private Label outputDirLabel;
    @FXML
    private TextField outputDirTextField;
    @FXML
    private Button outputDirButton;
    @FXML
    private HBox keyFileHbox;
    @FXML
    private Label keyFileLabel;
    @FXML
    private TextField keyFileTextField;
    @FXML
    private Button keyFileButton;
    @FXML
    private HBox serialHbox;
    @FXML
    private Label serialLabel;
    @FXML
    private TextField serialTextField;
    @FXML
    private Button decryptButton;

    @FXML
    protected void selectEbookFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open eBook File");
        infile = fileChooser.showOpenDialog(ebookFileButton.getScene().getWindow()).toPath();
        ebookFileTextField.setText(infile.toString());
    }

    @FXML
    protected void selectOutputDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Directory");
        outdir = directoryChooser.showDialog(outputDirButton.getScene().getWindow()).toPath();
        outputDirTextField.setText(outdir.toString());
    }

    @FXML
    protected void selectKeyFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Key File");
        keyfile = fileChooser.showOpenDialog(keyFileButton.getScene().getWindow()).toPath();
        keyFileTextField.setText(keyfile.toString());
    }

    @FXML
    protected void decryptBook() {
        serial = serialTextField.getText();
        String infile = ebookFileTextField.getText();
        String outdir = outputDirTextField.getText();
        String keyfile = keyFileTextField.getText();
        DeDRM.decryptBookWithKDatabaseAndSerial(infile, outdir, keyfile, serial);
    }
}
