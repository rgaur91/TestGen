package org.testgen.ui.controller;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.apache.commons.text.StringSubstitutor;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.testgen.db.DB;
import org.testgen.db.model.AuthConfig;
import org.testgen.db.model.User;
import org.testgen.rest.RestClient;
import org.testgen.ui.screens.AuthScreen;
import org.testgen.ui.screens.HomeScreen;

import java.util.HashMap;
import java.util.Map;

import static org.testgen.util.Assertion.assertNotBlank;

public class AuthCurdController {
    @FXML
    private TextField hostField;
    @FXML
    private TextField endpointField;
    @FXML
    private TextArea requestBodyTxtArea;
    @FXML
    private TextArea responseBodyTxtArea;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        System.out.println("Initialize method called Auth");
        AuthScreen.getInstance().setController(this);
    }

    public AuthConfig test() {
        AuthConfig authConfig = getAuthConfig();
        if (validate(authConfig)) {
            ObjectRepository<User> usersRepo = DB.getInstance().getDatabase().getRepository(User.class);
            Cursor<User> users = usersRepo.find();
            User user = users.firstOrDefault();
            Map<String, String> val= new HashMap<>();
            val.put("user.userName",user.getUserName());
            val.put("user.password",user.getPassword());
            String req = StringSubstitutor.replace(authConfig.getRequestBody(),val);
            String url = authConfig.getHost() +authConfig.getEndPoint();
            JsonObject res = RestClient.sendRequest(url, "POST", req, JsonObject.class);
            if (res==null){
                showError("Error in call the REST API");
            } else {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                responseBodyTxtArea.setText(gson.toJson(res));
                responseBodyTxtArea.setVisible(true);
                return authConfig;
            }
        }

        return null;
    }

    private boolean validate(AuthConfig authConfig) {
        try {
            assertNotBlank(authConfig.getHost(), "Host is required.");
            assertNotBlank(authConfig.getEndPoint(), "Endpoint is required.");
            assertNotBlank(authConfig.getRequestBody(), "Request Body is required");
            Gson gson =  new Gson();
            gson.fromJson(authConfig.getRequestBody(), JsonElement.class);
        } catch (IllegalArgumentException|JsonSyntaxException e){
            showError(e.getMessage());
        }

        return true;
    }

    private AuthConfig getAuthConfig() {
        AuthConfig authConfig = new AuthConfig();
        authConfig.setHost(hostField.getText());
        authConfig.setEndPoint(endpointField.getText());
        authConfig.setRequestBody(requestBodyTxtArea.getText());
        return authConfig;
    }

    public void close(ActionEvent actionEvent) {
        HomeScreen.getInstance().showHomeScreen();
    }

    public void saveAuth(ActionEvent actionEvent) {
        AuthConfig authConfig = test();
        try {
            if (authConfig != null) {
                ObjectRepository<AuthConfig> repository = DB.getInstance().getDatabase().getRepository(AuthConfig.class);
                repository.update(authConfig, true);
                showSuccess("Updated Successfully");
            }
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setVisible(true);
    }
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(true);
    }

    public void loadData() {
        ObjectRepository<AuthConfig> repository = DB.getInstance().getDatabase().getRepository(AuthConfig.class);
        Cursor<AuthConfig> authConfigs = repository.find();
        AuthConfig authConfig = authConfigs.firstOrDefault();
        if (authConfig !=null){
            hostField.setText(authConfig.getHost());
            endpointField.setText(authConfig.getEndPoint());
            requestBodyTxtArea.setText(authConfig.getRequestBody());
            responseBodyTxtArea.setText("");
        }
    }
}
