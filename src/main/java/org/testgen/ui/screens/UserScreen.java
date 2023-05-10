package org.testgen.ui.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.model.User;
import org.testgen.db.model.UserType;
import org.testgen.ui.controller.UserCurdController;

import java.io.IOException;

public class UserScreen extends ConfigTableScreen<User> {
    private static volatile UserScreen instance = null;
    private StackPane pane;
    private TableView<User> tableView;
    private UserCurdController controller;
    private Pagination pagination;

    private UserScreen() throws IOException {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + UserScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
    }


    @NotNull
    @Override
    protected EventHandler<ActionEvent> getDelEventHandler(User data) {
        return event -> {
            try {
                boolean delete = controller.delete(data);
                if (delete) {
                    getTableView().getItems().remove(data);
                }  else {
                    showError("Unknown error occurred.");
                }
            } catch (IllegalArgumentException e){
                showError(e.getMessage());
            }
        };
    }

    @Override
    public FindOptions getSortOn() {
        return FindOptions.sort("name", SortOrder.Ascending);
    }

    public void setController(UserCurdController userCurdController) {
        this.controller= userCurdController;
    }

    public static UserScreen getInstance(){
        if (instance == null) {
            synchronized (UserScreen.class) {
                if (instance == null) {
                    try {
                        System.out.println("Creating New Instance");
                        instance = new UserScreen();
                        instance.pane =FXMLLoader.load(instance.getClass().getResource("/ui.screens/UserConfig.fxml"));
                        GridPane userTablePane = (GridPane) instance.pane.getChildren().get(0);
//        <TableView id="userTable" fx:id="userTable" editable="true" prefHeight="241.0" prefWidth="560.0" tableMenuButtonVisible="true" GridPane.columnIndex="1" GridPane.rowIndex="3">
                        instance.tableView = instance.createTable();
                        instance.tableView.setId("userTable");
                        TableColumn<User, String> column = instance.createColumn("Name", "name");
                        TableColumn<User, String> column1 = instance.createColumn("User Name", "userName");
                        TableColumn<User, String> column2= instance.createColumn("Password", "password");
                        TableColumn<User, UserType> column3 = instance.createColumn("User Type", "userType", UserType.class);
                        TableColumn<User, String> column4= instance.createColumn("Auth Cross Checker", "authCheckUser");
                        TableColumn<User, User> deleteColumn = instance.createDeleteColumn();
                        instance.tableView.getColumns().addAll(column,column1, column2, column3, column4, deleteColumn);
                        instance.pagination = new Pagination();
                        userTablePane.add(instance.tableView, 1,3,1,1);
                        userTablePane.add(instance.pagination, 1,4,5,1);
                        UserCurdController.reloadTable(instance.tableView, User.class, instance.pagination, instance.getSortOn());
                        instance.addErrorLabel();
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

    @Override
    public Pagination getPagination() {
        return pagination;
    }
}