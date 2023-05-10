package org.testgen.ui.controller;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import org.dizitart.no2.Filter;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.model.DataSource;
import org.testgen.db.model.FieldType;
import org.testgen.db.model.SourcedField;
import org.testgen.rest.RestClient;
import org.testgen.ui.screens.SourceScreen;
import org.testgen.util.AuthUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private TextArea responseBodyTxtArea;

    @FXML
    private CheckBox authentication;

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
        return true;
    }

    @NotNull
    String getScreenPath() {
        return "/ui.screens/AddEditSource.fxml";
    }

    public void saveSource(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
        DataSource ds = getDataSource();
        if(validate(ds)){
            ObjectRepository<DataSource> repository = getRepository();
            repository.insert(ds);
            showSuccess("Source saved.");
            updateFields(ds);
        }
    }

    public void updateFields(DataSource ds) {

        AuthUtil authUtil = AuthUtil.getInstance();

        JsonObject res = RestClient.sendRequest(authUtil.getHost()+ds.getEndpoint(), ds.getMethod(), ds.getRequestBody(), Map.of("X-Auth-Token", authUtil.getToken()), JsonObject.class);
        List<SourcedField> fields= new LinkedList<>();
        for (String fName : res.keySet()) {
            JsonElement jsonElement = res.get(fName);
            fields.add(new SourcedField(fName,ds.getSourceName(), fName, getType(jsonElement)));
        }
        System.out.println(res.keySet());
        ObjectRepository<SourcedField> fieldRepo = getRepository(SourcedField.class);
        for (SourcedField field : fields) {
            fieldRepo.update(field, true);
        }
        // delete non-exiting fields
        Iterator<SourcedField> iterator = fieldRepo.find(FindOptions.limit(0, 100)).iterator();
        while (iterator.hasNext()){
            SourcedField sourcedField = iterator.next();
            if (!fields.contains(sourcedField)) {
                iterator.remove();
                System.out.println("Removed: "+sourcedField);
            }
        }
    }

    private FieldType getType(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            return FieldType.ARRAY;
        } else if (jsonElement.isJsonObject()) {
            return FieldType.OBJECT;
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jp = jsonElement.getAsJsonPrimitive();
            if (jp.isNumber()) {
               return FieldType.NUMBER;
            } else if (jp.isBoolean()) {
                return FieldType.BOOLEAN;
            } else if (jp.isString()) {
                return FieldType.STRING;
            }
        }
        return FieldType.NULL;
    }


    /*private void processJsonElement(List<SourcedField> interceptedData, JsonObject parent, JsonElement jsonElement, String name) {
        if (!jsonElement.isJsonNull()) {
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                boolean haltngo = true;
                for (JsonElement element : jsonArray) {
                    if (element.isJsonPrimitive()) {
                        if (haltngo && (haltngo = (AuthorizationItem.getValue(name) == null))) {
                            break;
                        }
                        addPrimitive(interceptedData, parent, element, name);
                    } else if (element.isJsonObject()) {
                        processJsonElement(interceptedData, parent, element, name);
                    }
                }
            } else if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
                for (Map.Entry<String, JsonElement> entry : entries) {
                    JsonElement value = entry.getValue();
                    if (!value.isJsonNull()) {
                        String key = entry.getKey();
                        if (value.isJsonPrimitive()) {
                            addPrimitive(interceptedData, jsonObject, value, key);
                        } else {
                            if(AuthorizationItem.getMetrics().contains(key.toLowerCase())){
                                processMetrics(value);
                            }
                            processJsonElement(interceptedData, jsonObject, value, key);
                        }
                    }
                }
            }
        }
    }*/

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

        AuthUtil authUtil = AuthUtil.getInstance();
        JsonObject res = RestClient.sendRequest(authUtil.getHost()+ds.getEndpoint(), ds.getMethod(), ds.getRequestBody(), Map.of("X-Auth-Token", authUtil.getToken()), JsonObject.class);
        if (res==null){
            showError("Error in call the REST API");
            return false;
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            responseBodyTxtArea.setText(gson.toJson(res));
            return true;
        }
    }



    private DataSource getDataSource() {
        DataSource ds = new DataSource();
        ds.setSourceName(sourceNameField.getText());
        ds.setEndpoint(endpointField.getText());
        ds.setMethod(methodChoice.getValue());
        ds.setRequestBody(requestBodyTxtArea.getText());
        ds.setAuthentication(authentication.isSelected());
        return ds;
    }


    public void testApi(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
        DataSource ds = getDataSource();
        validate(ds);
    }

    public List<String> getSourceNames(String source) {
        ObjectRepository<DataSource> repository = getRepository();
        Cursor<DataSource> dataSources = repository.find();
        System.out.println("All Items: "+dataSources.size());
        List<String> names =  new LinkedList<>();
        dataSources.iterator().forEachRemaining(s->{
            if (s.getSourceName().toLowerCase().startsWith(source.toLowerCase())) {
                names.add(s.getSourceName());
            }
        });
        return names;
    }
}
