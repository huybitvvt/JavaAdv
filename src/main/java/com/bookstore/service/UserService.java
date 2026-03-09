package com.bookstore.service;

import com.bookstore.dao.UserDAO;
import com.bookstore.dao.EmployeeDAO;
import com.bookstore.model.User;
import com.bookstore.model.Employee;
import com.bookstore.util.PasswordUtil;

import java.sql.SQLException;

/**
 * User Service - Business logic layer for User (Authentication)
 */
public class UserService {

    private final UserDAO userDAO;
    private final EmployeeDAO employeeDAO;
    private User currentUser;

    public UserService() {
        this.userDAO = new UserDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    /**
     * Đăng nhập
     */
    public User login(String username, String password) {
        try {
            // Mã hóa password sang MD5
            String hashedPassword = PasswordUtil.md5(password);
            User user = userDAO.login(username, hashedPassword);
            if (user != null) {
                // Load employee information
                Employee employee = employeeDAO.getById(user.getMaNhanVien());
                user.setNhanVien(employee);
                this.currentUser = user;
            }
            return user;
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return null;
        }
    }

    /**
     * Đăng xuất
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Lấy thông tin user hiện tại
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Kiểm tra đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Kiểm tra có phải Admin không
     */
    public boolean isAdmin() {
        return currentUser != null && "Admin".equals(currentUser.getVaiTro());
    }

    /**
     * Đổi mật khẩu
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        try {
            if (currentUser == null) {
                System.err.println("No user logged in");
                return false;
            }

            // Verify old password
            User user = userDAO.getById(currentUser.getMaTaiKhoan());
            if (!user.getMatKhau().equals(oldPassword)) {
                System.err.println("Old password is incorrect");
                return false;
            }

            return userDAO.changePassword(currentUser.getMaTaiKhoan(), newPassword);
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean isUsernameExists(String username) {
        try {
            return userDAO.isUsernameExists(username);
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tạo tài khoản mới
     */
    public boolean createUser(User user) {
        try {
            if (user.getTenDangNhap() == null || user.getTenDangNhap().trim().isEmpty()) {
                System.err.println("Username is required");
                return false;
            }
            if (userDAO.isUsernameExists(user.getTenDangNhap())) {
                System.err.println("Username already exists");
                return false;
            }
            return userDAO.insert(user);
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy tất cả user
     */
    public java.util.List<User> getAllUsers() {
        try {
            return userDAO.getAll();
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            return null;
        }
    }
}
