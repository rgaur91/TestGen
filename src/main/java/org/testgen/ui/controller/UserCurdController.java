package org.testgen.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.DB;
import org.testgen.db.model.User;
import org.testgen.ui.screens.UserScreen;

public class UserCurdController extends AbstractCurdController<User, UserScreen> {

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

    @NotNull
    @Override
    String getScreenPath() {
        return "/ui.screens/AddEditUser.fxml";
    }

    @Override
    protected UserScreen getScreen() {
        return UserScreen.getInstance();
    }
}
