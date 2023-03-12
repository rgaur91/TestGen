package org.testgen.ui.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;

public class OverlayController {
    public GridPane overlay;

    public void addOverlay(ActionEvent actionEvent) {
        overlay.setVisible(true);
    }
}
