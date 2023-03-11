package org.testgen.ui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class MappingController {

    @FXML
    private TextField jsonInput;

    @FXML
    private VBox mappings;

    @FXML
    private Label status;

    private Map<String, String> fieldToColumnMap = new HashMap<>();

    @FXML
    private void parseJson() {
        String json = jsonInput.getText();
        if (json == null || json.trim().isEmpty()) {
            status.setText("Please enter a valid JSON input.");
            return;
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        mappings.getChildren().clear();
        fieldToColumnMap.clear();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            Label label = new Label("JSON Key: " + key);
            TextField textField = new TextField();
            Button button = new Button("Map to DB Column");
            button.setOnAction(event -> {
                String dbColumn = textField.getText();
                if (dbColumn == null || dbColumn.trim().isEmpty()) {
                    status.setText("Please enter a valid database column name.");
                } else {
                    fieldToColumnMap.put(key, dbColumn);
                    status.setText("Mapping saved for JSON key '" + key + "'.");
                }
            });
            VBox hbox = new VBox(label, textField, button);
            mappings.getChildren().add(hbox);
        }

        status.setText("JSON input parsed successfully.");
    }

    @FXML
    private void saveMappings() {
        if (fieldToColumnMap.isEmpty()) {
            status.setText("No mappings to save.");
        } else {
            // Replace with your own code to save mappings to the database
            status.setText("Mappings saved successfully.");
        }
    }
}
