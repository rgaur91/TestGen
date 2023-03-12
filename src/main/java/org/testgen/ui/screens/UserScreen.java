package org.testgen.ui.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.testgen.db.model.User;
import org.testgen.db.model.UserType;
import org.testgen.ui.controller.UserCurdController;

import java.io.IOException;

public class UserScreen extends ConfigTableScreen<User> {
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
        tableView = createTable();
        tableView.setId("userTable");
        TableColumn<User, String> column = createColumn("Name", "name");
            TableColumn<User, String> column1 = createColumn("User Name", "userName");
            TableColumn<User, String> column2= createColumn("Password", "password");
            TableColumn<User, UserType> column3 = createColumn("User Type", "userType", UserType.class);
            TableColumn<User, String> column4= createColumn("Auth Cross Checker", "authCheckUser");
        tableView.getColumns().addAll(column,column1, column2, column3, column4);
        userTablePane.add(tableView, 1,3,1,1);
    }


    public static UserScreen getInstance(){
        if (instance == null) {
            synchronized (UserScreen.class) {
                if (instance == null) {
                    try {
                        instance = new UserScreen();
                        UserCurdController.reloadTable(instance.tableView, User.class);
                    } catch (IOException e) {
                        throw new RuntimeException("Not able to initialize User screen",e);
                    }
                }
            }
        }
        return instance;
    }

    @Override
    public StackPane getPane() {
        return pane;
    }

    @Override
    public TableView<User> getTableView() {
        return tableView;
    }
}