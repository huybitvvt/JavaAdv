package com.bookstore.controller;

import com.bookstore.service.ImportService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

/**
 * Import Controller
 */
public class ImportController {

    @FXML
    private TableView<?> tableImports;

    private ImportService importService;

    public ImportController() {
        this.importService = new ImportService();
    }

    @FXML
    private void handleAdd() {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng nhập hàng");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
