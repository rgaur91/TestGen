package org.testgen.ui.screens;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.StringUtils;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.jetbrains.annotations.NotNull;
import org.testgen.db.model.SourcedField;
import org.testgen.ui.controller.FieldCurdController;

import java.io.IOException;

public class FieldsScreen extends ConfigTableScreen<SourcedField> {
    private static volatile FieldsScreen instance = null;
    private StackPane pane;
    private TableView<SourcedField> tableView;
    private final FieldCurdController fieldCurdController;
    private ContextMenu sourceContext;
    private Pagination pagination;

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
                        TableColumn<SourcedField, String> column = instance.createColumn("Name", "name");
                        column.setCellFactory(TextFieldTableCell.forTableColumn());
                        column.setOnEditCommit(e->{
                            if (!StringUtils.isBlank(e.getOldValue())) {
                                instance.showError("Name cannot be edited");
                            } else {
                                e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
                            }
                        });
                        TableColumn<SourcedField, String> column1= instance.createColumn("Source", "source");
                        TableColumn<SourcedField, String> column2= instance.createColumn("Path", "path");
                        TableColumn<SourcedField, String> column3= instance.createColumn("Type", "type");
                        instance.tableView.getColumns().addAll(column,column1, column2, column3);
                        instance.pagination = new Pagination();
                        sourceTablePane.add(instance.tableView, 1,3,5,1);
                        sourceTablePane.add(instance.pagination, 1,4,5,1);
                        FieldCurdController.reloadTable(instance.tableView, SourcedField.class, instance.pagination, instance.getSortOn());
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
    public TableView<SourcedField> getTableView() {
        return tableView;
    }

    @Override
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    protected EventHandler<ActionEvent> getDelEventHandler(SourcedField data) {
        return null;
    }

    @Override
    public FindOptions getSortOn() {
        return FindOptions.sort("name", SortOrder.Ascending);
    }
}