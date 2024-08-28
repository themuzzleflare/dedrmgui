/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import cloud.tavitian.dedrmtools.DeDRM;
import cloud.tavitian.dedrmtools.Debug;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public final class KindleDeDRMController extends VBox {
    private final HBox decryptHbox;
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

    public KindleDeDRMController() {
        ebookFileLabel = new Label("eBook File:");

        ebookFileTextField = new TextField();
        ebookFileTextField.setPromptText("eBook File");

        ebookFileButton = new Button("Select");
        ebookFileButton.setOnAction(event -> selectEbookFile());

        ebookFileHbox = new HBox(5.0, ebookFileLabel, ebookFileTextField, ebookFileButton);
        ebookFileHbox.setAlignment(Pos.CENTER);

        outputDirLabel = new Label("Output Directory:");

        outputDirTextField = new TextField();
        outputDirTextField.setPromptText("Output Directory");

        outputDirButton = new Button("Select");
        outputDirButton.setOnAction(event -> selectOutputDir());

        outputDirHbox = new HBox(5.0, outputDirLabel, outputDirTextField, outputDirButton);
        outputDirHbox.setAlignment(Pos.CENTER);

        keyFileLabel = new Label("Key File:");

        keyFileTextField = new TextField();
        keyFileTextField.setPromptText(".k4i File");

        keyFileButton = new Button("Select");
        keyFileButton.setOnAction(event -> selectKeyFile());

        keyFileHbox = new HBox(5.0, keyFileLabel, keyFileTextField, keyFileButton);
        keyFileHbox.setAlignment(Pos.CENTER);

        serialLabel = new Label("Serial:");

        serialTextField = new TextField();
        serialTextField.setPromptText("Kindle Serial Number");

        serialHbox = new HBox(5.0, serialLabel, serialTextField);
        serialHbox.setAlignment(Pos.CENTER);

        decryptButton = new Button("Decrypt");
        decryptButton.setOnAction(event -> decryptBook());

        decryptButton.disableProperty().bind(
                ebookFileTextField.textProperty().isEmpty().or(
                        outputDirTextField.textProperty().isEmpty().or(
                                keyFileTextField.textProperty().isEmpty().and(
                                        serialTextField.textProperty().isEmpty()
                                )
                        )
                )
        );

        debugCheckBox = new CheckBox("Debug");

        decryptHbox = new HBox(5.0, decryptButton, debugCheckBox);
        decryptHbox.setAlignment(Pos.CENTER);

        setSpacing(20.0);
        setAlignment(Pos.CENTER);

        getChildren().addAll(ebookFileHbox, outputDirHbox, keyFileHbox, serialHbox, decryptHbox);
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


    private void decryptBook() {
        String serial = serialTextField.getText();
        String infile = ebookFileTextField.getText();
        String outdir = outputDirTextField.getText();
        String keyfile = keyFileTextField.getText();

        System.out.println("Serial: " + serial);
        System.out.println("Input File: " + infile);
        System.out.println("Output Directory: " + outdir);
        System.out.println("Key File: " + keyfile);

        Debug.setDebug(debugCheckBox.isSelected());

        DeDRM.decryptBookWithKDatabaseAndSerial(infile, outdir, keyfile, serial);
    }
}
