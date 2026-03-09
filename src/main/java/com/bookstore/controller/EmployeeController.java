package com.bookstore.controller;

import com.bookstore.model.Employee;
import com.bookstore.service.EmployeeService;
import com.bookstore.util.FormatUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

/**
 * Employee Controller
 */
public class EmployeeController {

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Employee> tableEmployees;
    @FXML
    private TableColumn<Employee, Integer> colMaNV;
    @FXML
    private TableColumn<Employee, String> colHoTen;
    @FXML
    private TableColumn<Employee, String> colGioiTinh;
    @FXML
    private TableColumn<Employee, String> colSDT;
    @FXML
    private TableColumn<Employee, String> colEmail;
    @FXML
    private TableColumn<Employee, String> colChucVu;
    @FXML
    private TableColumn<Employee, Double> colLuong;
    @FXML
    private TableColumn<Employee, String> colTrangThai;
    @FXML
    private TableColumn<Employee, String> colHanhDong;

    private EmployeeService employeeService;
    private ObservableList<Employee> employeeList;

    public EmployeeController() {
        this.employeeService = new EmployeeService();
        this.employeeList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadEmployees();
    }

    private void setupTableColumns() {
        colMaNV.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        colLuong.setCellValueFactory(new PropertyValueFactory<>("luong"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        colLuong.setCellFactory(column -> new TableCell<Employee, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(FormatUtil.formatCurrency(item));
                }
            }
        });

        colHanhDong.setCellFactory(column -> new TableCell<Employee, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Employee emp = getTableView().getItems().get(getIndex());
                    javafx.scene.layout.HBox actions = new javafx.scene.layout.HBox(5);
                    Button btnEdit = new Button("Sửa");
                    btnEdit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    btnEdit.setOnAction(e -> handleEdit(emp));

                    Button btnDelete = new Button("Xóa");
                    btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    btnDelete.setOnAction(e -> handleDelete(emp));

                    actions.getChildren().addAll(btnEdit, btnDelete);
                    setGraphic(actions);
                }
            }
        });
    }

    private void loadEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees != null) {
            employeeList.clear();
            employeeList.addAll(employees);
            tableEmployees.setItems(employeeList);
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (!keyword.isEmpty()) {
            List<Employee> employees = employeeService.searchEmployees(keyword);
            employeeList.clear();
            if (employees != null) employeeList.addAll(employees);
        } else {
            loadEmployees();
        }
    }

    @FXML
    private void handleAdd() {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng thêm nhân viên");
    }

    @FXML
    private void handleEdit(Employee employee) {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng sửa nhân viên");
    }

    @FXML
    private void handleDelete(Employee employee) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa nhân viên");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa nhân viên: " + employee.getHoTen() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (employeeService.deleteEmployee(employee.getMaNhanVien())) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa nhân viên!");
                loadEmployees();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa nhân viên!");
            }
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
