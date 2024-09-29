/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final class BufferedTextAreaOutputStream extends OutputStream {
    private static final int FLUSH_THRESHOLD = 8192; // Flush buffer when it reaches this size
    private static final int FLUSH_INTERVAL_MS = 100; // Time-based flushing (in ms)
    private final TextArea textArea;
    private final StringBuilder buffer = new StringBuilder();
    private final StringBuilder pendingText = new StringBuilder();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public BufferedTextAreaOutputStream(TextArea textArea) {
        this.textArea = textArea;

        // Start a periodic flush task that flushes the pendingText buffer
        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    TimeUnit.MILLISECONDS.sleep(FLUSH_INTERVAL_MS);
                    flushPendingText();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    public void write(int b) {
        synchronized (buffer) {
            buffer.append((char) b);

            if (buffer.length() >= FLUSH_THRESHOLD || b == '\n') {
                flushBuffer();
            }
        }
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) {
        synchronized (buffer) {
            buffer.append(new String(b, off, len));
            if (buffer.length() >= FLUSH_THRESHOLD || buffer.toString().contains("\n")) {
                flushBuffer();
            }
        }
    }

    @Override
    public void flush() {
        flushBuffer();
    }

    private void flushBuffer() {
        synchronized (buffer) {
            pendingText.append(buffer);
            buffer.setLength(0); // Clear the buffer
        }
    }

    private void flushPendingText() {
        synchronized (pendingText) {
            if (!pendingText.isEmpty()) {
                String textToAppend = pendingText.toString();
                pendingText.setLength(0); // Clear the pendingText buffer

                Platform.runLater(() -> textArea.appendText(textToAppend));
            }
        }
    }

    @Override
    public void close() throws IOException {
        shutdown();
        super.close();
    }

    // Call this to properly shut down the executor when the application stops
    private void shutdown() {
        executor.shutdownNow();
    }
}
