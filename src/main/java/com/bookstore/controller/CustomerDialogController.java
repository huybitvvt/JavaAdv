package com.bookstore.controller;

import com.bookstore.model.Customer;
import com.bookstore.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Date;

/**
 * Customer Dialog Controller
 */
public class CustomerDialogController {

    @FXML
    private TextField txtHoTen;
    @FXML
    private ComboBox<String> cboGioiTinh;
    @FXML
    private DatePicker dpNgaySinh;
    @FXML
    private TextField txtSDT;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtDiaChi;

    private Customer customer;
    private CustomerService customerService;

    public CustomerDialogController() {
        this.customerService = new CustomerService();
    }

    @FXML
    public void initialize() {
        cboGioiTinh.getItems().addAll("Nam", "Nữ", "Khác");
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            loadCustomerData();
        }
    }

    private void loadCustomerData() {
        txtHoTen.setText(customer.getHoTen());
        cboGioiTinh.setValue(customer.getGioiTinh());
        txtSDT.setText(customer.getSoDienThoai());
        txtEmail.setText(customer.getEmail());
        txtDiaChi.setText(customer.getDiaChi());
    }

    @FXML
    private void handleSave() {
        if (txtHoTen.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập họ tên!");
            return;
        }

        try {
            if (customer == null) {
                customer = new Customer();
            }

            customer.setHoTen(txtHoTen.getText().trim());
            customer.setGioiTinh(cboGioiTinh.getValue());
            customer.setSoDienThoai(txtSDT.getText().trim());
            customer.setEmail(txtEmail.getText().trim());
            customer.setDiaChi(txtDiaChi.getText().trim());

            boolean success;
            if (customer.getMaKhachHang() == 0) {
                success = customerService.addCustomer(customer);
            } else {
                success = customerService.updateCustomer(customer);
            }

            if (success) {
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể lưu!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtHoTen.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
