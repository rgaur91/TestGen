package org.testgen.ui.screens;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import jdk.jfr.EventFactory;
import org.testgen.db.model.User;
import org.testgen.db.model.UserType;
import org.testgen.ui.controller.UserCurdController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserScreen {
    private static volatile UserScreen instance = null;
    private final StackPane pane;
    private final TableView<User> tableView;

    private UserScreen() throws IOException {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + UserScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
        this.pane =FXMLLoader.load(getClass().getResource("/ui.screens/UserConfig.fxml"));
        GridPane userTablePane = (GridPane) pane.getChildren().get(0);
//        <TableView id="userTable" fx:id="userTable" editable="true" prefHeight="241.0" prefWidth="560.0" tableMenuButtonVisible="true" GridPane.columnIndex="1" GridPane.rowIndex="3">
        tableView = new TableView<>();
        tableView.setId("userTable");
        tableView.setEditable(true);
        tableView.setPrefHeight(260);
        tableView.setPrefWidth(560);

            TableColumn<User, String> column = createColumn("Name", "name");
            TableColumn<User, String> column1 = createColumn("User Name", "userName");
            TableColumn<User, String> column2= createColumn("Password", "password");
            TableColumn<User, UserType> column3 = createColumn("User Type", "userType", UserType.class);
            TableColumn<User, String> column4= createColumn("Auth Cross Checker", "authCheckUser");
        tableView.getColumns().addAll(column,column1, column2, column3, column4);
        userTablePane.add(tableView, 1,3,1,1);
    }

    private TableColumn<User, String> createColumn(String name, String factoryName) {
        return createColumn(name, factoryName, String.class);
    }

    private <T> TableColumn<User, T> createColumn(String name, String factoryName, Class<T> tClass) {
        TableColumn<User, T> column1 =
                new TableColumn<>(name);
        column1.setCellValueFactory(
                new PropertyValueFactory<>(factoryName));
        return column1;
    }

    public static UserScreen getInstance(){
        if (instance == null) {
            synchronized (UserScreen.class) {
                if (instance == null) {
                    try {
                        instance = new UserScreen();
                        UserCurdController.reloadTable();
                    } catch (IOException e) {
                        throw new RuntimeException("Not able to initialize User screen",e);
                    }
                }
            }
        }
        return instance;
    }

    public StackPane getPane() {
        return pane;
    }

    public TableView<User> getTableView() {
        return tableView;
    }
}