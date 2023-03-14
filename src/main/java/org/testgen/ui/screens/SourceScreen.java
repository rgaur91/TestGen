package org.testgen.ui.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.testgen.db.model.DataSource;
import org.testgen.ui.controller.SourceCurdController;

import java.io.IOException;

public class SourceScreen extends ConfigTableScreen<DataSource> {
    private static volatile SourceScreen instance = null;
    private StackPane pane;
    private TableView<DataSource> tableView;
    private SourceCurdController controller;

    private SourceScreen() throws IOException {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + SourceScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
    }

    public static SourceScreen getInstance() {
        if (instance == null) {
            synchronized (SourceScreen.class) {
                if (instance == null) {
                    try {
                        instance = new SourceScreen();
                        instance.pane = FXMLLoader.load(instance.getClass().getResource("/ui.screens/SourceConfig.fxml"));
                        GridPane sourceTablePane = (GridPane) instance.pane.getChildren().get(0);
                        instance.tableView = instance.createTable();
                        instance.tableView.setId("sourceTable");

                        TableColumn<DataSource, String> column = instance.createColumn("Source Name", "sourceName");
                        TableColumn<DataSource, String> column1= instance.createColumn("Endpoint", "endpoint");
                        TableColumn<DataSource, String> column2= instance.createColumn("Method", "method");
                        TableColumn<DataSource, String> column3= instance.createColumn("Request", "requestBody");
                        instance.tableView.getColumns().addAll(column,column1, column2, column3);
                        sourceTablePane.add(instance.tableView, 1,3,1,1);
                        SourceCurdController.reloadTable(instance.tableView, DataSource.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Not able to initialize Source screen");
                    }
                }
            }
        }
        return instance;
    }

    public StackPane getPane() {
        return pane;
    }

    public TableView<DataSource> getTableView() {
        return tableView;
    }

    @Override
    protected EventHandler<ActionEvent> getDelEventHandler(DataSource data) {
        return event -> {
            boolean delete = controller.delete(data);
            if (delete) {
                getTableView().getItems().remove(data);
            }
        };
    }

    public void setController(SourceCurdController controller) {
        this.controller = controller;
    }

    public SourceCurdController getController() {
        return controller;
    }
}