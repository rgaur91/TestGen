package org.testgen.ui.screens;

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
    private final StackPane pane;
    private final TableView<DataSource> tableView;

    private SourceScreen() throws IOException {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + SourceScreen.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
        this.pane = FXMLLoader.load(getClass().getResource("/ui.screens/SourceConfig.fxml"));
        GridPane sourceTablePane = (GridPane) pane.getChildren().get(0);
        tableView = createTable();
        tableView.setId("sourceTable");

        TableColumn<DataSource, String> column = createColumn("Source Name", "sourceName");
        TableColumn<DataSource, String> column1 = createColumn("Endpoint", "endpoint");
        TableColumn<DataSource, String> column2= createColumn("Method", "method");
        TableColumn<DataSource, String> column3= createColumn("Request", "requestBody");
        tableView.getColumns().addAll(column,column1, column2, column3);
        sourceTablePane.add(tableView, 1,3,1,1);
    }

    public static SourceScreen getInstance() {
        if (instance == null) {
            synchronized (SourceScreen.class) {
                if (instance == null) {
                    try {
                        instance = new SourceScreen();
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
}