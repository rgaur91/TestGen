package org.testgen.ui.controller;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.testgen.ui.screens.FieldsScreen;
import org.testgen.ui.screens.HomeScreen;
import org.testgen.ui.screens.SourceScreen;
import org.testgen.ui.screens.UserScreen;

public class MenuController {


    @FXML
    private void handleUserConfig() {
        updateScreen(MenuScreens.USER);
    }

    @FXML
    private void handleSourceConfig() {
        updateScreen(MenuScreens.SOURCE);
    }

    @FXML
    private void handleFieldsConfig() {
        updateScreen(MenuScreens.FIELD);
    }

    private void updateScreen(MenuScreens screen) {
        String javaLibPath = System.getProperty("java.library.path");
        System.out.println(javaLibPath);
        BorderPane borderPane = HomeScreen.getInstance().getMainFrame();
        switch (screen) {
            case USER:
                borderPane.setCenter(UserScreen.getInstance().getPane());
                break;
            case SOURCE:
                borderPane.setCenter(SourceScreen.getInstance().getPane());
                break;
            case FIELD:
                borderPane.setCenter(FieldsScreen.getInstance().getPane());
                break;
            default:
                HomeScreen.getInstance().showHomeScreen();
                break;
        }
    }


    private enum MenuScreens {
        USER,SOURCE,FIELD

    }
}
