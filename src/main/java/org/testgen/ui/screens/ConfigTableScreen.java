package org.testgen.ui.screens;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public abstract class ConfigTableScreen<T> {

    public abstract StackPane getPane();
    public abstract TableView<T> getTableView();

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
}
