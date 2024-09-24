/*
 * Copyright © 2024 Paul Tavitian.
 */

package cloud.tavitian.dedrmgui;

import javafx.geometry.Orientation;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

final class Spacer extends Region {
    /**
     * Creates a new horizontally oriented Spacer that expands
     * to fill remaining space.
     */
    public Spacer() {
        this(Orientation.HORIZONTAL);
    }

    /**
     * Creates a new Spacer with the given orientation that expands
     * to fill remaining space.
     *
     * @param orientation The orientation of the spacer.
     */
    public Spacer(Orientation orientation) {
        super();

        switch (orientation) {
            case HORIZONTAL -> HBox.setHgrow(this, Priority.ALWAYS);
            case VERTICAL -> VBox.setVgrow(this, Priority.ALWAYS);
        }
    }

    /**
     * Creates a new Spacer with the fixed size.
     *
     * @param size The size of the spacer.
     */
    @SuppressWarnings("unused")
    public Spacer(double size) {
        this(size, Orientation.HORIZONTAL);
    }

    /**
     * Creates a new Spacer with the fixed size and orientation.
     *
     * @param size        The size of the spacer.
     * @param orientation The orientation of the spacer.
     */
    public Spacer(double size, Orientation orientation) {
        super();

        switch (orientation) {
            case HORIZONTAL -> {
                setMinWidth(size);
                setPrefWidth(size);
                setMaxWidth(size);
            }
            case VERTICAL -> {
                setMinHeight(size);
                setPrefHeight(size);
                setMaxHeight(size);
            }
        }
    }
}
