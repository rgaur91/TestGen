package org.testgen.ui.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.testgen.ui.controller.AuthCurdController;

import java.io.IOException;

public class AuthScreen {
    private static volatile AuthScreen instance = null;
    private GridPane pane;
    private AuthCurdController controller;

    private AuthScreen() {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + AuthScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
    }

    public static AuthScreen getInstance() {
        if (instance == null) {
            synchronized (AuthScreen.class) {
                if (instance == null) {
                    try {
                        instance = new AuthScreen();
                        instance.pane = FXMLLoader.load(instance.getClass().getResource("/ui.screens/AuthConfig.fxml"));
                        instance.controller.loadData();
                    } catch (IOException e) {
                        throw new RuntimeException("Not able to initialize User screen",e);
                    }
                }
            }
        }
        return instance;
    }

    public Node getPane() {
        return pane;
    }

    public void setController(AuthCurdController controller) {
        this.controller = controller;
    }

    public AuthCurdController getController() {
        return controller;
    }
}