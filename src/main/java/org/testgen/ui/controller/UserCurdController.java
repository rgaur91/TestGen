package org.testgen.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.model.User;
import org.testgen.db.model.UserType;
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

    @FXML
    protected void initialize() {
        System.out.println("Initialize method called");
        UserScreen.getInstance().setController(this);
    }


    public void saveUser(ActionEvent actionEvent) {
        errorLabel.setText("");
        User user = getUser();
        if (validate(user)) {
            ObjectRepository<User> repository = getUserObjectRepository();
            repository.insert(user);
           errorLabel.setText("User saved successfully.");

       }
    }

    @NotNull
    private User getUser() {
        User user = new User();
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String type = typeBox.getValue();
        user.setName(name);
        user.setUserName(username);
        user.setPassword(password);
        user.setUserType(UserType.valueOf(type));
        user.setAuthCheckUser(counterAuthField.getText());
        return user;
    }

    private boolean validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            errorLabel.setText("Name is required field");
            return false;
        }

        if (user.getUserName() == null || user.getUserName().isBlank()) {
            errorLabel.setText("Please enter a username.");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            errorLabel.setText("Please enter a password.");
            return false;
        }
        if (user.getUserType() == null) {
            errorLabel.setText("Please select a user type.");
            return false;
        }
        if (user.getAuthCheckUser() != null && !user.getAuthCheckUser().isBlank()) {
            User crossUser = getUserByName(user.getAuthCheckUser());
            if (crossUser == null) {
                errorLabel.setText("Cross Auth user does not exist.");
                return false;
            }
        }
       return true;
    }

    private User getUserByName(String name) {
        ObjectRepository<User> repository = getUserObjectRepository();
        Cursor<User> cursor = repository.find();
        return repository.find(ObjectFilters.eq("name",name)).firstOrDefault();
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

    @Override
    protected boolean validateDelete(ObjectRepository<User> repository, User data) {
        if (repository.find(ObjectFilters.eq("authCheckUser", data.getName())).size()>0) {
            throw new IllegalArgumentException("User  Cannot be deleted as it is referred for cross auth.");
        }
        return true;
    }
}
