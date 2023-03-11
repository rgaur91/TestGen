package org.testgen.ui.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.bson.Document;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.testgen.db.DB;
import org.testgen.db.model.User;
import org.testgen.db.model.UserType;
import org.testgen.ui.screens.UserScreen;

import java.io.IOException;
import java.util.List;

public class UserCurdController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> typeBox;

    @FXML
    private TextField counterAuthField;

    @FXML
    private Label errorLabel;

    public void addOverlay(ActionEvent actionEvent) {
        StackPane pane = UserScreen.getInstance().getPane();
        try {
            Node load = FXMLLoader.load(getClass().getResource("/ui.screens/AddEditUser.fxml"));
            StackPane.setMargin(load, new Insets(50,50,50,50));
            pane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeOverlay(ActionEvent actionEvent) {
        StackPane pane = UserScreen.getInstance().getPane();
        ObservableList<Node> children = pane.getChildren();
        System.out.println("Children in Usercurd "+children);
        children.removeIf(node -> "AddEditOverlay".equals(node.getId()));
        reloadTable();
    }

    public static void reloadTable() {
       List<User> users=getUsers();
        ObservableList<User> items = UserScreen.getInstance().getTableView().getItems();
        items.remove(0, items.size());
        items.addAll(users);
    }

    private static List<User> getUsers() {
        Nitrite database = DB.getInstance().getDatabase();
        ObjectRepository<User> repository = database.getRepository(User.class);
        Cursor<User> users = repository.find(FindOptions.limit(0, 5));
        return users.toList();
    }

    public void saveUser(ActionEvent actionEvent) {
        errorLabel.setText("");
       if (validate()) {
           String name = nameField.getText();
           String username = usernameField.getText();
           String password = passwordField.getText();
           String type = typeBox.getValue();

           DB db = DB.getInstance();
           Nitrite database = db.getDatabase();
           ObjectRepository<User> repository = database.getRepository(User.class);
//            repository.insert(new User(name, username,password, UserType.valueOf(type)));

           errorLabel.setText("User saved successfully.");

       }
    }

    private boolean validate() {
        String name = nameField.getText();
        if (name == null || name.isBlank()) {
            errorLabel.setText("Name is required field");
            return false;
        }
        String username = usernameField.getText();
        if (username.isEmpty() || username.isBlank()) {
            errorLabel.setText("Please enter a username.");
            return false;
        }
        String password = passwordField.getText();
        if (password.isEmpty() || password.isBlank()) {
            errorLabel.setText("Please enter a password.");
            return false;
        }
        String type = typeBox.getValue();

        if (type == null || type.isBlank()) {
            errorLabel.setText("Please select a user type.");
            return false;
        }
       return true;
    }
}
