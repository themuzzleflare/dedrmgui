/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

final class SettingsDict extends LinkedHashMap<String, String> {
    private static final Gson gson = new Gson();

    private static final String inputFileKey = "inputFile";
    private static final String outputFileKey = "outputFile";
    private static final String keyFileKey = "keyFile";
    private static final String serialKey = "serial";

    public SettingsDict() {
        super();
    }

    public SettingsDict(File file) throws IOException {
        this(file.getAbsolutePath());
    }

    public SettingsDict(String filename) throws IOException {
        super();
        putAll(loadFromFile(filename));
    }

    public SettingsDict(String inputFile, String outputFile, String keyFile, String serial) {
        super();
        setInputFile(inputFile);
        setOutputFile(outputFile);
        setKeyFile(keyFile);
        setSerial(serial);
    }

    public static SettingsDict loadFromFile(File file) throws IOException {
        return loadFromFile(file.getAbsolutePath());
    }

    public static SettingsDict loadFromFile(String filename) throws IOException {
        return gson.fromJson(new FileReader(filename), SettingsDict.class);
    }

    public void writeToFile(File file) throws IOException {
        writeToFile(file.getAbsolutePath());
    }

    public void writeToFile(String filename) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        gson.toJson(this, fileWriter);
        fileWriter.close();
    }

    public String getInputFile() {
        return get(inputFileKey);
    }

    public void setInputFile(String inputFile) {
        put(inputFileKey, inputFile);
    }

    public String getOutputFile() {
        return get(outputFileKey);
    }

    public void setOutputFile(String outputFile) {
        put(outputFileKey, outputFile);
    }

    public String getKeyFile() {
        return get(keyFileKey);
    }

    public void setKeyFile(String keyFile) {
        put(keyFileKey, keyFile);
    }

    public String getSerial() {
        return get(serialKey);
    }

    public void setSerial(String serial) {
        put(serialKey, serial);
    }
}
