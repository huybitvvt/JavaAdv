package com.bookstore.controller;

import com.bookstore.model.Customer;
import com.bookstore.service.CustomerService;
import com.bookstore.util.FormatUtil;
import com.bookstore.util.ExportExcelUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Customer Controller - Xử lý quản lý khách hàng
 */
public class CustomerController {

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Customer> tableCustomers;
    @FXML
    private TableColumn<Customer, Integer> colMaKH;
    @FXML
    private TableColumn<Customer, String> colHoTen;
    @FXML
    private TableColumn<Customer, String> colGioiTinh;
    @FXML
    private TableColumn<Customer, String> colNgaySinh;
    @FXML
    private TableColumn<Customer, String> colSDT;
    @FXML
    private TableColumn<Customer, String> colEmail;
    @FXML
    private TableColumn<Customer, String> colDiaChi;
    @FXML
    private TableColumn<Customer, Integer> colDiem;
    @FXML
    private TableColumn<Customer, String> colHanhDong;

    private CustomerService customerService;
    private ObservableList<Customer> customerList;

    public CustomerController() {
        this.customerService = new CustomerService();
        this.customerList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCustomers();
    }

    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colDiem.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));

        colNgaySinh.setCellFactory(column -> new TableCell<Customer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setText("");
                } else {
                    Customer c = getTableView().getItems().get(getIndex());
                    setText(FormatUtil.formatDate(c.getNgaySinh()));
                }
            }
        });

        colHanhDong.setCellFactory(column -> new TableCell<Customer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Customer customer = getTableView().getItems().get(getIndex());
                    javafx.scene.layout.HBox actions = new javafx.scene.layout.HBox(5);
                    Button btnEdit = new Button("Sửa");
                    btnEdit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    btnEdit.setOnAction(e -> handleEdit(customer));

                    Button btnDelete = new Button("Xóa");
                    btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    btnDelete.setOnAction(e -> handleDelete(customer));

                    actions.getChildren().addAll(btnEdit, btnDelete);
                    setGraphic(actions);
                }
            }
        });
    }

    private void loadCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers != null) {
            customerList.clear();
            customerList.addAll(customers);
            tableCustomers.setItems(customerList);
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (!keyword.isEmpty()) {
            List<Customer> customers = customerService.searchCustomers(keyword);
            customerList.clear();
            if (customers != null) {
                customerList.addAll(customers);
            }
        } else {
            loadCustomers();
        }
    }

    @FXML
    private void handleAdd() {
        openCustomerDialog(null);
    }

    @FXML
    private void handleEdit(Customer customer) {
        openCustomerDialog(customer);
    }

    @FXML
    private void handleDelete(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa khách hàng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa khách hàng: " + customer.getHoTen() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (customerService.deleteCustomer(customer.getMaKhachHang())) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa khách hàng!");
                loadCustomers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa khách hàng!");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        loadCustomers();
    }

    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("DanhSachKhachHang_" + System.currentTimeMillis() + ".xlsx");

        File file = fileChooser.showSaveDialog(tableCustomers.getScene().getWindow());
        if (file != null) {
            try {
                List<Customer> customers = customerService.getAllCustomers();
                if (customers != null && !customers.isEmpty()) {
                    boolean success = ExportExcelUtil.exportCustomers(customers, file.getAbsolutePath());
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất Excel thành công!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Xuất Excel thất bại!");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Thông báo", "Không có dữ liệu để xuất!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xuất Excel: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void openCustomerDialog(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bookstore/view/customer_dialog.fxml"));
            Parent root = loader.load();

            CustomerDialogController controller = loader.getController();
            controller.setCustomer(customer);

            Stage stage = new Stage();
            stage.setTitle(customer == null ? "Thêm khách hàng" : "Sửa khách hàng");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCustomers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
