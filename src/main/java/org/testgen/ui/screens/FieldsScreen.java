package org.testgen.ui.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class FieldsScreen {
    private static volatile FieldsScreen instance = null;
    private final GridPane pane;

    private FieldsScreen() throws IOException {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + FieldsScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
        this.pane = FXMLLoader.load(getClass().getResource("/ui.screens/FieldsConfig.fxml"));

    }

    public static FieldsScreen getInstance() {
        if (instance == null) {
            synchronized (FieldsScreen.class) {
                if (instance == null) {
                    try {
                        instance = new FieldsScreen();
                    } catch (IOException e) {
                        throw new RuntimeException("Not able to initialize FieldsScreen screen");
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