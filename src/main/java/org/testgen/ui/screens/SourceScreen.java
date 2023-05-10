package org.testgen.ui.screens;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.testgen.db.model.DataSource;
import org.testgen.ui.controller.SourceCurdController;

import java.io.IOException;

public class SourceScreen extends ConfigTableScreen<DataSource> {
    private static volatile SourceScreen instance = null;
    private StackPane pane;
    private TableView<DataSource> tableView;
    private SourceCurdController controller;
    private Pagination pagination;

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
                        TableColumn<DataSource, DataSource> deleteColumn = instance.createDeleteColumn();
//                        System.out.println("Pref Hight"+deleteColumn.getMaxWidth()+"; pref width:"+deleteColumn.getPrefWidth());
                        TableColumn<DataSource, DataSource> reloadFields = instance.createReloadFields();
                        instance.tableView.getColumns().addAll(column,column1, column2, column3, deleteColumn, reloadFields);
                        instance.pagination = new Pagination();
                        sourceTablePane.add(instance.tableView, 1,3,1,1);
                        sourceTablePane.add(instance.pagination, 1,4,5,1);

                        SourceCurdController.reloadTable(instance.tableView, DataSource.class, instance.pagination, instance.getSortOn());
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
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    protected EventHandler<ActionEvent> getDelEventHandler(DataSource data) {
        return event -> {
            System.out.println("Inside delete Source");
            boolean delete = controller.delete(data);
            if (delete) {
                getTableView().getItems().remove(data);
            }
        };
    }

    @Override
    public FindOptions getSortOn() {
        return FindOptions.sort("sourceName", SortOrder.Ascending);
    }

    protected TableColumn<DataSource, DataSource> createReloadFields() {
        TableColumn<DataSource, DataSource> deleteCol = new TableColumn<>("Reload Fields");
        deleteCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> new TableCell<DataSource, DataSource>() {
            private final Button reloadButton = new Button();
            {
                Image img = new Image(SourceScreen.class.getResourceAsStream("/reload.png"));
                ImageView view = new ImageView(img);
                view.setFitHeight(20);
                view.setPreserveRatio(true);
//                Creating a Button
//                reloadButton.setTranslateX(200);
//                reloadButton.setTranslateY(25);
//                Setting the size of the button
                reloadButton.setPrefSize(30, 20);
//                Setting a graphic to the button
                reloadButton.setGraphic(view);
            }


            @Override
            protected void updateItem(DataSource data, boolean empty) {
                super.updateItem(data, empty);

                if (data == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(reloadButton);
                reloadButton.setOnAction(
                        getReloadEventHandler(data)
                );
            }
        });
        return deleteCol;
    }

    private EventHandler<ActionEvent> getReloadEventHandler(DataSource data) {
        return event -> {
            System.out.println("Inside reload Source fields");
            controller.updateFields(data);
        };
    }

    public void setController(SourceCurdController controller) {
        this.controller = controller;
    }

    public SourceCurdController getController() {
        return controller;
    }

}