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
    private static final Gson GSON = new Gson();

    private static final String INPUTFILE_KEY = "inputFile";
    private static final String OUTPUTFILE_KEY = "outputFile";
    private static final String KEYFILE_KEY = "keyFile";
    private static final String SERIAL_KEY = "serial";

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public static SettingsDict loadFromFile(File file) throws IOException {
        return loadFromFile(file.getAbsolutePath());
    }

    public static SettingsDict loadFromFile(String filename) throws IOException {
        return GSON.fromJson(new FileReader(filename), SettingsDict.class);
    }

    public void writeToFile(File file) throws IOException {
        writeToFile(file.getAbsolutePath());
    }

    public void writeToFile(String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            GSON.toJson(this, fileWriter);
        }
    }

    public String getInputFile() {
        return get(INPUTFILE_KEY);
    }

    public void setInputFile(String inputFile) {
        put(INPUTFILE_KEY, inputFile);
    }

    public String getOutputFile() {
        return get(OUTPUTFILE_KEY);
    }

    public void setOutputFile(String outputFile) {
        put(OUTPUTFILE_KEY, outputFile);
    }

    public String getKeyFile() {
        return get(KEYFILE_KEY);
    }

    public void setKeyFile(String keyFile) {
        put(KEYFILE_KEY, keyFile);
    }

    public String getSerial() {
        return get(SERIAL_KEY);
    }

    public void setSerial(String serial) {
        put(SERIAL_KEY, serial);
    }
}
