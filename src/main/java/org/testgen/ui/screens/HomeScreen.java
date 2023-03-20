package org.testgen.ui.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.testgen.ui.controller.MenuController;

import java.io.IOException;

public class HomeScreen {

    private static HomeScreen instance;
    private static final String lock = "HomeScreen";
    private BorderPane mainFrame;
    private GridPane defaultScreen;
    private Scene scene;
    private MenuController controller;

    private HomeScreen() {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + HomeScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
    }

    public static HomeScreen getInstance() {
        if (instance == null) {
            synchronized (lock) {
                try {
                    instance = new HomeScreen();
                    instance.init();
                } catch (IOException e) {
                    throw new RuntimeException("Not able to initialize HomeScreen.");
                }
            }
        }
        return instance;
    }

    private void init() throws IOException {
        mainFrame = new BorderPane();
        this.scene = new Scene(mainFrame, 900, 600);
        this.defaultScreen = FXMLLoader.load(getClass().getResource("/ui.screens/HomeScreen.fxml"));
        // Load the Mainframe screen from the FXML file
        addMenu(mainFrame);
        mainFrame.setCenter(defaultScreen);
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
        homeButton.setOnAction(e -> showHomeScreen());
        HBox hBox = new HBox(menuBar, homeButton);
        mainFrm.setTop(hBox);

    }

    public void showHomeScreen() {
        // Set home screen as the center of the root layout.
        mainFrame.setCenter(defaultScreen);
    }

    public void setController(MenuController controller) {
        this.controller = controller;
    }

    public MenuController getController() {
        return controller;
    }
}
