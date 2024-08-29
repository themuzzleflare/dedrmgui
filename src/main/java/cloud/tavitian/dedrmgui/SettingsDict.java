/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import java.util.LinkedHashMap;

final class SettingsDict extends LinkedHashMap<String, String> {
    private static final String inputFileKey = "inputFile";
    private static final String outputFileKey = "outputFile";
    private static final String keyFileKey = "keyFile";
    private static final String serialKey = "serial";

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
