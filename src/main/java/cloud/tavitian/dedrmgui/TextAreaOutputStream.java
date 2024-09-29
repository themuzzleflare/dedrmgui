/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
final class TextAreaOutputStream extends OutputStream {
    private static final int FLUSH_THRESHOLD = 8192; // Flush when buffer reaches this size
    private final TextArea textArea;
    private final StringBuilder buffer = new StringBuilder();

    public TextAreaOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        buffer.append((char) b);

        if (b == '\n' || buffer.length() >= FLUSH_THRESHOLD) {
            flushBuffer();
        }
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) {
        String text = new String(b, off, len, StandardCharsets.UTF_8);
        buffer.append(text);

        if (text.contains("\n") || buffer.length() >= FLUSH_THRESHOLD) {
            flushBuffer();
        }
    }

    @Override
    public void flush() {
        flushBuffer();
    }

    private void flushBuffer() {
        if (buffer.isEmpty()) {
            return;
        }

        String text = buffer.toString();
        buffer.setLength(0);

        Platform.runLater(() -> textArea.appendText(text));
    }
}
