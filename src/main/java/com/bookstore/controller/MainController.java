package com.bookstore.controller;

import com.bookstore.model.User;
import com.bookstore.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main Controller - Điều hướng chính
 */
public class MainController {

    @FXML
    private Label lblUserName;
    @FXML
    private Label lblUserRole;
    @FXML
    private Label lblStatus;
    @FXML
    private AnchorPane contentArea;

    private User currentUser;

    /**
     * Set current user and update UI
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInfo();
        loadDashboardView();
    }

    /**
     * Update user info display
     */
    private void updateUserInfo() {
        if (currentUser != null) {
            String role = "Admin".equals(currentUser.getVaiTro()) ? "Quản trị viên" : "Nhân viên";
            lblUserName.setText("Xin chào, " + currentUser.getTenDangNhap());
            lblUserRole.setText(role);
        }
    }

    // Navigation methods
    @FXML
    public void showDashboard() {
        // Reload dashboard into content area
        loadDashboardView();
        lblStatus.setText("Tổng quan");
    }

    @FXML
    public void showBookManagement() {
        loadView("/com/bookstore/view/book.fxml");
        lblStatus.setText("Quản lý sách");
    }

    @FXML
    public void showCustomerManagement() {
        loadView("/com/bookstore/view/customer.fxml");
        lblStatus.setText("Quản lý khách hàng");
    }

    @FXML
    public void showEmployeeManagement() {
        loadView("/com/bookstore/view/employee.fxml");
        lblStatus.setText("Quản lý nhân viên");
    }

    @FXML
    public void showSupplierManagement() {
        loadView("/com/bookstore/view/supplier.fxml");
        lblStatus.setText("Quản lý nhà cung cấp");
    }

    @FXML
    public void showPOS() {
        loadView("/com/bookstore/view/pos.fxml");
        lblStatus.setText("Bán hàng");
    }

    @FXML
    public void showInvoiceManagement() {
        loadView("/com/bookstore/view/invoice.fxml");
        lblStatus.setText("Quản lý hóa đơn");
    }

    @FXML
    public void showImportManagement() {
        loadView("/com/bookstore/view/import.fxml");
        lblStatus.setText("Quản lý nhập hàng");
    }

    @FXML
    public void showRevenueStatistics() {
        loadView("/com/bookstore/view/statistics.fxml");
        lblStatus.setText("Thống kê doanh thu");
    }

    @FXML
    public void showInventoryStatistics() {
        loadView("/com/bookstore/view/inventory.fxml");
        lblStatus.setText("Thống kê tồn kho");
    }

    /**
     * Load dashboard view (from dashboard.fxml)
     */
    private void loadDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bookstore/view/dashboard.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load a view into content area
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void showChangePasswordDialog() {
        // TODO: Implement change password dialog
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chức năng đổi mật khẩu");
    }

    @FXML
    public void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bookstore/view/login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) lblUserName.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(root));
                stage.setMaximized(true);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thoát");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn thoát?");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    public void showAbout() {
        showAlert(Alert.AlertType.INFORMATION, "Giới thiệu",
                "Quản Lý Cửa Hàng Sách\nVersion 1.0\n\nCreated for Java Advanced Final Project");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
