/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.Locale;

final class TextAreaPrintStream extends PrintStream {
    private final TextArea textArea;

    public TextAreaPrintStream(TextArea textArea) {
        super(System.out);
        this.textArea = textArea;
    }

    @Override
    public void print(boolean b) {
        Platform.runLater(() -> textArea.appendText(Boolean.toString(b)));
    }

    @Override
    public void print(char c) {
        Platform.runLater(() -> textArea.appendText(Character.toString(c)));
    }

    @Override
    public void print(int i) {
        Platform.runLater(() -> textArea.appendText(Integer.toString(i)));
    }

    @Override
    public void print(long l) {
        Platform.runLater(() -> textArea.appendText(Long.toString(l)));
    }

    @Override
    public void print(float f) {
        Platform.runLater(() -> textArea.appendText(Float.toString(f)));
    }

    @Override
    public void print(char @NotNull [] s) {
        Platform.runLater(() -> textArea.appendText(new String(s)));
    }

    @Override
    public void print(double d) {
        Platform.runLater(() -> textArea.appendText(Double.toString(d)));
    }

    @Override
    public void print(String s) {
        Platform.runLater(() -> textArea.appendText(s));
    }

    @Override
    public void print(Object obj) {
        Platform.runLater(() -> textArea.appendText(obj.toString()));
    }

    @Override
    public void println() {
        Platform.runLater(() -> textArea.appendText("\n"));
    }

    @Override
    public void println(boolean x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(char x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(int x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(long x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(float x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(double x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(char @NotNull [] x) {
        Platform.runLater(() -> textArea.appendText(new String(x) + "\n"));
    }

    @Override
    public void println(String x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public void println(Object x) {
        Platform.runLater(() -> textArea.appendText(x + "\n"));
    }

    @Override
    public PrintStream printf(@NotNull String format, Object... args) {
        Platform.runLater(() -> textArea.appendText(String.format(format, args)));
        return null;
    }

    @Override
    public PrintStream printf(Locale l, @NotNull String format, Object... args) {
        Platform.runLater(() -> textArea.appendText(String.format(l, format, args)));
        return null;
    }
}
