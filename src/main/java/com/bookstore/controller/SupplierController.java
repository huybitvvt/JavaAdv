package com.bookstore.controller;

import com.bookstore.model.Supplier;
import com.bookstore.service.SupplierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

/**
 * Supplier Controller
 */
public class SupplierController {

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Supplier> tableSuppliers;

    private SupplierService supplierService;
    private ObservableList<Supplier> supplierList;

    public SupplierController() {
        this.supplierService = new SupplierService();
        this.supplierList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        tableSuppliers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("maNhaCungCap"));
        tableSuppliers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("tenNhaCungCap"));
        tableSuppliers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        tableSuppliers.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        tableSuppliers.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("email"));
        tableSuppliers.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nguoiLienHe"));

        loadSuppliers();
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers != null) {
            supplierList.clear();
            supplierList.addAll(suppliers);
            tableSuppliers.setItems(supplierList);
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (!keyword.isEmpty()) {
            List<Supplier> suppliers = supplierService.searchSuppliers(keyword);
            supplierList.clear();
            if (suppliers != null) supplierList.addAll(suppliers);
        } else {
            loadSuppliers();
        }
    }

    @FXML
    private void handleAdd() {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng thêm NCC");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
