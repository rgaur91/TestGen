package org.testgen.ui.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.model.DataSource;
import org.testgen.ui.screens.SourceScreen;

public class SourceCurdController extends AbstractCurdController<DataSource, SourceScreen>{

    @FXML
    private TextField sourceNameField;
    @FXML
    private TextField endpointField;
    @FXML
    private ChoiceBox methodChoice;
    @FXML
    private Label errorLabel;
    @FXML
    private TextArea requestBodyTxtArea;

    @FXML
    protected void initialize() {
        System.out.println("Initialize method called");
        System.out.println(requestBodyTxtArea);
        /*requestBodyTxtArea.textProperty().addListener ((observable, oldValue, newValue) -> {
            try {
                // Try to parse the entered text as a JSON object
                Gson jsonObject =  new Gson();
                JsonElement jsonElement = jsonObject.fromJson(newValue, JsonElement.class);
                // If parsing is successful, set the text area style to default
                requestBodyTxtArea.setStyle("-fx-control-inner-background: white;");
            } catch (JsonSyntaxException e) {
                // If parsing fails, set the text area style to indicate an error
                System.out.println(e.getMessage());
                errorLabel.setText(e.getMessage());
                requestBodyTxtArea.setStyle("-fx-control-inner-background: pink;");
            }
        });*/
}

    @Override
    protected SourceScreen getScreen() {
        return SourceScreen.getInstance();
    }

    @NotNull
    String getScreenPath() {
        return "/ui.screens/AddEditSource.fxml";
    }

    public void saveSource(ActionEvent actionEvent) {

    }
}
