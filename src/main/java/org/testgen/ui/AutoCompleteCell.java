package org.testgen.ui;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Side;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import org.apache.commons.lang3.StringUtils;
import org.testgen.ui.controller.SourceCurdController;
import org.testgen.ui.screens.SourceScreen;

import java.util.List;
import java.util.stream.Collectors;

public class AutoCompleteCell<T> extends TextFieldTableCell<T, String> {

    private TextField textField;

    public AutoCompleteCell() {
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            textField = createAutoCompleteTextField();
        }

        textField.setText(getItem());
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem());
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                textField.setText(getString());
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }

    private TextField createAutoCompleteTextField() {
        TextField textField = new TextField();
        textField.setPrefWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        ContextMenu sourceContext = new ContextMenu();
        textField.setContextMenu(sourceContext);

        // Filter data based on the user input
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            try {

                System.out.println("Inside change listener");

                if (!sourceContext.isShowing()) { //optional
                    System.out.println("Now Showing");
                    sourceContext.show(textField, Side.BOTTOM, 0, 0); //position of popup
                }

                // Try to parse the entered text as a JSON object
                if (!StringUtils.isBlank(newValue)) {
                    String[] paths = StringUtils.split(newValue, '.');
                    if (paths.length==1){
                        // find similar
                        sourceContext.getItems().clear();
                        SourceCurdController dataHandler = SourceScreen.getInstance().getController();
                        List<String> sourceNames = dataHandler.getSourceNames(newValue);
                        System.out.println("Found sources with "+newValue+" :"+ sourceNames);
                        if (!sourceNames.isEmpty()) {
                            sourceContext.getItems().addAll(sourceNames.stream().map(MenuItem::new).collect(Collectors.toList()));
                        }
                    } else {
                        sourceContext.getItems().clear();
                        sourceContext.getItems().addAll(new MenuItem("newValue1"),new MenuItem("newValue2"),new MenuItem("newValue3"),new MenuItem("newValue4"));
                    }
                }
            } catch (Exception e) {
                // If parsing fails, set the text area style to indicate an error
                System.out.println(e.getMessage());
//                instance.showError(e.getMessage());
            }
        });

        // Commit the new value when the user presses Enter
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
        sourceContext.setOnAction(e->textField.setText(((MenuItem)e.getTarget()).getText()));
        return textField;
    }
}
