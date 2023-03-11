package org.testgen.ui.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.testgen.ui.TestGenApp;

import java.io.IOException;

public class HomeScreen {

    private static HomeScreen instance;
    private static final String lock= "HomeScreen";
    private final BorderPane mainFrame;
    private final GridPane defaultScreen;
    private final Scene scene;

    public static HomeScreen getInstance() {
        if (instance==null) {
            synchronized (lock) {
                try {
                    instance = new HomeScreen();
                } catch (IOException e) {
                    throw new RuntimeException("Not able to initialize HomeScreen.");
                }
            }
        }
        return instance;
    }
    private HomeScreen() throws IOException {
        mainFrame = new BorderPane();
        this.scene = new Scene(mainFrame, 400, 400);
        this.defaultScreen =FXMLLoader.load(getClass().getResource("/ui.screens/HomeScreen.fxml"));
        init();
    }

    private void init() {
        try {

            // Load the Mainframe screen from the FXML file
            addMenu(mainFrame);
            mainFrame.setCenter(defaultScreen);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }

    public BorderPane getMainFrame() {
        return mainFrame;
    }

    private void addMenu(BorderPane mainFrm) throws IOException {
        MenuBar menuBar = FXMLLoader.load(getClass().getResource("/ui.screens/Menus.fxml"));
//        FileInputStream input = new FileInputStream(getClass().getResource("/menu.png").getFile());
//        Image image = new Image(input);
//        ImageView imageView = new ImageView(image);
//        menuBar.getMenus().get(0).setGraphic(imageView);
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e->showHomeScreen());
        HBox hBox = new HBox(menuBar,homeButton);
        mainFrm.setTop(hBox);

    }
    public void showHomeScreen() {
            // Set home screen as the center of the root layout.
            mainFrame.setCenter(defaultScreen);
    }
}
