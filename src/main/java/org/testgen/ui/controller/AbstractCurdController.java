package org.testgen.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.testgen.db.DB;
import org.testgen.ui.screens.ConfigTableScreen;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.testgen.ui.screens.ConfigTableScreen.PAGE_SIZE;

public abstract class AbstractCurdController<T,S extends ConfigTableScreen<T>> {

    public static final String ADD_EDIT_OVERLAY = "AddEditOverlay";
    public static final String OVERLAY = "Overlay";

    @FXML
    protected Label errorLabel;

    public void addOverlay(ActionEvent actionEvent) {
        StackPane pane = getScreen().getPane();
        try {
            Node load = FXMLLoader.load(getClass().getResource(getScreenPath()));
            StackPane.setMargin(load, new Insets(50,50,50,50));
            pane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    abstract String getScreenPath();

    public void closeOverlay(ActionEvent actionEvent) {
        System.out.println("Close overlay called");
        StackPane pane = getScreen().getPane();
        ObservableList<Node> children = pane.getChildren();
        children.stream().filter(c->OVERLAY.equals(c.getId())).forEach(n->n.setVisible(false));
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        reloadTable(getScreen().getTableView(), clazz, getScreen().getPagination(), getScreen().getSortOn());
    }

    protected abstract S getScreen();

    public static <X> void reloadTable(TableView<X> tableView, Class<X> tClass, Pagination pagination, FindOptions sortOn) {
        long dataSize=getDataSize(tClass);
        System.out.println("Data Size:"+dataSize);
        // Set first page on load
        ObservableList<X> firstItems = tableView.getItems();
        firstItems.clear();
        firstItems.addAll(getData(tClass,0, sortOn));
        pagination.setPageCount((int) (dataSize/PAGE_SIZE+1));
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            ObservableList<X> items = tableView.getItems();
            items.clear();
            items.addAll(getData(tClass,newIndex.intValue() * PAGE_SIZE, sortOn));
        });
    }


    private static <X> long getDataSize(Class<X> tClass) {
        Nitrite database = DB.getInstance().getDatabase();
        ObjectRepository<X> repository = database.getRepository(tClass);
        return repository.size();
    }

    private static <X> List<X> getData(Class<X> tClass, int offset, FindOptions sortOn) {
        Nitrite database = DB.getInstance().getDatabase();
        ObjectRepository<X> repository = database.getRepository(tClass);
        Cursor<X> users = repository.find(sortOn.thenLimit(offset, ConfigTableScreen.PAGE_SIZE));
        return users.toList();
    }

    protected ObjectRepository<T> getRepository() {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        return getRepository(clazz);
    }

    protected <D> ObjectRepository<D> getRepository(Class<D> clazz) {
        DB db = DB.getInstance();
        Nitrite database = db.getDatabase();
        return database.getRepository(clazz);
    }

    public boolean delete(T data) {
        ObjectRepository<T> repository = getRepository();
        if(validateDelete(repository, data)) {
            WriteResult result = repository.remove(data);
            System.out.println("Deleted rows:"+ result.getAffectedCount());
            return result.getAffectedCount() > 0;
        }
        return false;
    }

    protected void showError(String error) {
        errorLabel.setText(error+" X");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(true);
    }

    protected void showSuccess(String msg) {
        errorLabel.setText(msg);
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setVisible(true);
    }

    protected abstract boolean validateDelete(ObjectRepository<T> repository, T data);
}
