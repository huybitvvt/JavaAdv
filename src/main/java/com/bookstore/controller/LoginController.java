package com.bookstore.controller;

import com.bookstore.model.User;
import com.bookstore.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Login Controller - Xử lý đăng nhập
 */
public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;

    private UserService userService;

    public LoginController() {
        this.userService = new UserService();
    }

    /**
     * Xử lý đăng nhập
     */
    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        // Validation
        if (username.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập!");
            return;
        }

        if (password.isEmpty()) {
            showError("Vui lòng nhập mật khẩu!");
            return;
        }

        // Login
        User user = userService.login(username, password);

        if (user != null) {
            // Login successful - load main screen
            loadMainScreen(user);
        } else {
            showError("Tên đăng nhập hoặc mật khẩu không đúng!");
            txtPassword.clear();
            txtPassword.requestFocus();
        }
    }

    /**
     * Hiển thị thông báo lỗi
     */
    private void showError(String message) {
        lblError.setText(message);
    }

    /**
     * Load màn hình chính
     */
    private void loadMainScreen(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bookstore/view/main.fxml"));
            Parent root = loader.load();

            // Get main controller and set current user
            MainController mainController = loader.getController();
            mainController.setCurrentUser(user);

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/bookstore/css/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải màn hình chính: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hiển thị thông báo Alert
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
