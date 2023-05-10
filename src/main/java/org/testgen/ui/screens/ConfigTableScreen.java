package org.testgen.ui.screens;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import org.dizitart.no2.FindOptions;

public abstract class ConfigTableScreen<T> {

    public static final int PAGE_SIZE = 10;
    protected Label errorLabel;

    public abstract StackPane getPane();
    public abstract TableView<T> getTableView();
    public abstract Pagination getPagination();

    protected TableColumn<T, String> createColumn(String name, String factoryName) {
        return createColumn(name, factoryName, String.class);
    }

    protected <N> TableColumn<T, N> createColumn(String name, String factoryName, Class<N> tClass) {
        TableColumn<T, N> column1 =
                new TableColumn<>(name);
        column1.setCellValueFactory(
                new PropertyValueFactory<>(factoryName));
        return column1;
    }

    protected TableView<T> createTable() {
        TableView<T> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setPrefHeight(260);
        tableView.setPrefWidth(560);
        return tableView;
    }

    protected TableColumn<T, T> createDeleteColumn() {
        TableColumn<T, T> deleteCol = new TableColumn<>("Action");
        deleteCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> new TableCell<T, T>() {
            private final Button deleteButton = new Button("X");

            @Override
            protected void updateItem(T data, boolean empty) {
                super.updateItem(data, empty);

                if (data == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        getDelEventHandler(data)
                );
            }
        });
        return deleteCol;
    }


    protected void addErrorLabel() {
//        <Label disable="true" style="-fx-text-fill: brown" wrapText="true"/>
        errorLabel= new Label();
        errorLabel.setTextFill(Paint.valueOf("brown"));
        errorLabel.setOnMouseClicked(e->((Label)e.getSource()).setVisible(false));
        StackPane.setMargin(errorLabel, new Insets(300,50,50,50));
        getPane().getChildren().add(errorLabel);

    }

    protected abstract EventHandler<ActionEvent> getDelEventHandler(T data);

    protected void showError(String error) {
        errorLabel.setText(error+" X");
        errorLabel.setVisible(true);
    }

    public abstract FindOptions getSortOn();
}
