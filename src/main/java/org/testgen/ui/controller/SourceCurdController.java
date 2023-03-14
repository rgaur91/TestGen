package org.testgen.ui.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.model.DataSource;
import org.testgen.rest.RestClient;
import org.testgen.ui.screens.SourceScreen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SourceCurdController extends AbstractCurdController<DataSource, SourceScreen>{

    @FXML
    private TextField sourceNameField;
    @FXML
    private TextField endpointField;
    @FXML
    private ChoiceBox<String> methodChoice;

    @FXML
    private TextArea requestBodyTxtArea;

    @FXML
    protected void initialize() {
        System.out.println("Initialize method called");
        System.out.println(requestBodyTxtArea);
        SourceScreen.getInstance().setController(this);
        requestBodyTxtArea.textProperty().addListener ((observable, oldValue, newValue) -> {
            try {
                // Try to parse the entered text as a JSON object
                Gson jsonObject =  new Gson();
                JsonElement jsonElement = jsonObject.fromJson(newValue, JsonElement.class);
                errorLabel.setVisible(false);
                // If parsing is successful, set the text area style to default
                requestBodyTxtArea.setStyle("-fx-control-inner-background: white;");
            } catch (JsonSyntaxException e) {
                // If parsing fails, set the text area style to indicate an error
                System.out.println(e.getMessage());
                showError(e.getMessage());
                requestBodyTxtArea.setStyle("-fx-control-inner-background: pink;");
            }
        });
        errorLabel.setTextFill(Paint.valueOf("brown"));
        errorLabel.setOnMouseClicked(e->((Label)e.getSource()).setVisible(false));
}

    @Override
    protected SourceScreen getScreen() {
        return SourceScreen.getInstance();
    }

    @Override
    protected boolean validateDelete(ObjectRepository<DataSource> repository, DataSource data) {
        return false;
    }

    @NotNull
    String getScreenPath() {
        return "/ui.screens/AddEditSource.fxml";
    }

    public void saveSource(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
        DataSource ds = getDataSource();
        if(validate(ds)){

        }

    }

    private boolean validate(DataSource ds) {
        if (ds.getSourceName() == null || ds.getSourceName().isBlank()) {
            showError("Source name is required.");
            return false;
        }
        if (ds.getEndpoint() == null || ds.getEndpoint().isBlank()) {
            showError("Endpoint is required.");
            return false;
        }
        if (ds.getMethod() == null || ds.getMethod().isBlank()) {
            showError("Method is required.");
            return false;
        }
        JsonObject res = RestClient.sendRequest(ds.getEndpoint(), ds.getMethod(), ds.getRequestBody(), JsonObject.class);
        if (res==null){
            showError("Error in call the REST API");
            return false;
        }
        return false;
    }



    private DataSource getDataSource() {
        DataSource ds = new DataSource();
        ds.setSourceName(sourceNameField.getText());
        ds.setEndpoint(endpointField.getText());
        ds.setMethod(methodChoice.getValue());
        ds.setRequestBody(requestBodyTxtArea.getText());
        return ds;
    }


}
