package org.testgen.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
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

public abstract class AbstractCurdController<T,S extends ConfigTableScreen<T>> {

    public static final String ADD_EDIT_OVERLAY = "AddEditOverlay";
    public static final String OVERLAY = "Overlay";

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
        reloadTable(getScreen().getTableView(), clazz);
    }

    protected abstract S getScreen();

    public static <X> void reloadTable(TableView<X> tableView, Class<X> tClass) {
        List<X> data=getData(tClass);
        ObservableList<X> items = tableView.getItems();
        items.remove(0, items.size());
        items.addAll(data);
    }

    private static <X> List<X> getData(Class<X> tClass) {
        Nitrite database = DB.getInstance().getDatabase();
        ObjectRepository<X> repository = database.getRepository(tClass);
        Cursor<X> users = repository.find(FindOptions.limit(0, 5));
        return users.toList();
    }

    protected ObjectRepository<T> getUserObjectRepository() {
        DB db = DB.getInstance();
        Nitrite database = db.getDatabase();
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        return database.getRepository(clazz);
    }

    public boolean delete(T data) {
        ObjectRepository<T> repository = getUserObjectRepository();
        if(validateDelete(repository, data)) {
            WriteResult result = repository.remove(data);
            return result.getAffectedCount() > 0;
        }
        return false;
    }

    protected abstract boolean validateDelete(ObjectRepository<T> repository, T data);
}
