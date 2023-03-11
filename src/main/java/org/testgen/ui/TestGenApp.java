package org.testgen.ui;


import javafx.application.Application;
import javafx.stage.Stage;
import org.testgen.ui.screens.HomeScreen;

import java.io.File;


public class TestGenApp extends Application {
// public static final String WD= "$HOME/Library/TestGen";

    @Override
    public void start(Stage primaryStage) {
//        File outDir = new File(WD);
//        if (!outDir.exists()) {
//            outDir.mkdir();
//        }
        primaryStage.setTitle("JavaFX App");
        HomeScreen homeScreen = HomeScreen.getInstance();
        // Set the home screen as the default screen
        primaryStage.setScene(homeScreen.getScene());
        primaryStage.setTitle("JavaFX App");
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}