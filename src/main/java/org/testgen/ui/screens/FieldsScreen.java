package org.testgen.ui.screens;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.model.DataSource;
import org.testgen.db.model.Field;
import org.testgen.db.model.FieldType;
import org.testgen.ui.AutoCompleteCell;
import org.testgen.ui.controller.FieldCurdController;
import org.testgen.ui.controller.SourceCurdController;

import java.io.IOException;

public class FieldsScreen extends ConfigTableScreen<Field> {
    private static volatile FieldsScreen instance = null;
    private StackPane pane;
    private TableView<Field> tableView;
    private final FieldCurdController fieldCurdController;
    private ContextMenu sourceContext;

    private FieldsScreen() throws IOException {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + FieldsScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
        fieldCurdController = new FieldCurdController();
    }

    public static FieldsScreen getInstance() {
        if (instance == null) {
            synchronized (FieldsScreen.class) {
                if (instance == null) {
                    try {
                        instance = new FieldsScreen();
                        instance.pane = FXMLLoader.load(instance.getClass().getResource("/ui.screens/FieldsConfig.fxml"));
                        GridPane sourceTablePane = (GridPane) instance.pane.getChildren().get(0);
                        instance.tableView = instance.createTable();
                        instance.tableView.setId("sourceTable");
                        instance.tableView.setEditable(true);
                        TableColumn<Field, String> column = instance.createColumn("Name", "name");
                        column.setCellFactory(TextFieldTableCell.forTableColumn());
                        column.setOnEditCommit(e->{
                            if (!StringUtils.isBlank(e.getOldValue())) {
                                instance.showError("Name cannot be edited");
                            } else {
                                e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
                            }
                        });
                        TableColumn<Field, String> column1= instance.createColumn("Source", "source");
                        column1.setCellFactory(param -> new AutoCompleteCell<>());
                        column1.setOnEditCommit(e-> {
                            Field field = e.getTableView().getItems().get(e.getTablePosition().getRow());
                            field.setSource(e.getNewValue());
                            instance.fieldCurdController.save(field);
                        });
                        instance.sourceContext = new ContextMenu();
                        column1.setContextMenu(instance.sourceContext);
                        column1.textProperty().addListener(getSourceChangeListener());
                        TableColumn<Field, String> column2= instance.createColumn("Type", "type");
//                        column2.setCellFactory(TextFieldTableCell.forTableColumn());
                        column2.setOnEditCommit(e-> {
                            Field field = e.getTableView().getItems().get(e.getTablePosition().getRow());
                           try {
                               FieldType type = FieldType.valueOf(e.getNewValue());
                               field.setType(type);
                               instance.fieldCurdController.save(field);
                           } catch (IllegalArgumentException ex){
                               instance.showError("Invalid Field Type. Allowed Values: "+FieldType.allowedValues());
                           }
                        });
                        TableColumn<Field, Field> column3= instance.createDeleteColumn();
                        instance.tableView.getColumns().addAll(column,column1, column2, column3);
                        Button add = new Button("Add");
                        add.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                instance.tableView.getItems().add(new Field());

                            }
                        });
                        sourceTablePane.add(add, 1,2, 1,1);
                        sourceTablePane.add(instance.tableView, 1,3,5,20);
                        FieldCurdController.reloadTable(instance.tableView, Field.class);
                        instance.addErrorLabel();
                    } catch (IOException e) {
                        throw new RuntimeException("Not able to initialize FieldsScreen screen");
                    }
                }
            }
        }
        return instance;
    }

    @NotNull
    private static ChangeListener<String> getSourceChangeListener() {
        return (observable, oldValue, newValue) -> {
            try {

                System.out.println("Inside change listener");
                ContextMenu sourceContext = instance.sourceContext;
                if (!sourceContext.isShowing()) { //optional
                    System.out.println("Now Showing");
                    sourceContext.show(sourceContext.getOwnerNode(), Side.BOTTOM, 0, 0); //position of popup
                }

                // Try to parse the entered text as a JSON object
                if (!StringUtils.isBlank(newValue)) {
                    String[] paths = StringUtils.split(newValue, '.');
                    if (paths.length==1){
                        // find similar
                        sourceContext.getItems().clear();
                        sourceContext.getItems().addAll(new MenuItem(newValue));
                    } else {
                        sourceContext.getItems().clear();
                        sourceContext.getItems().addAll(new MenuItem("newValue1"),new MenuItem("newValue2"),new MenuItem("newValue3"),new MenuItem("newValue4"));
                    }
                }
            } catch (Exception e) {
                // If parsing fails, set the text area style to indicate an error
                System.out.println(e.getMessage());
                instance.showError(e.getMessage());
            }
        };
    }

    public StackPane getPane() {
        return pane;
    }

    @Override
    public TableView<Field> getTableView() {
        return null;
    }

    @Override
    protected EventHandler<ActionEvent> getDelEventHandler(Field data) {
        return null;
    }
}