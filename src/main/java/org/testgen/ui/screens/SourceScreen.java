package org.testgen.ui.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class SourceScreen {
    private static volatile SourceScreen instance = null;
    private final GridPane pane;

    private SourceScreen() throws IOException {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + SourceScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
        this.pane = FXMLLoader.load(getClass().getResource("/ui.screens/SourceConfig.fxml"));
    }

    public static SourceScreen getInstance() {
        if (instance == null) {
            synchronized (SourceScreen.class) {
                if (instance == null) {
                    try {
                        instance = new SourceScreen();
                    } catch (IOException e) {
                        throw new RuntimeException("Not able to initialize Source screen");
                    }
                }
            }
        }
        return instance;
    }

    public GridPane getPane() {
        return pane;
    }
}