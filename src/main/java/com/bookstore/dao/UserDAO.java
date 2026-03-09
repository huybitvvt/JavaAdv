package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User DAO - Data Access Object for User entity (Authentication)
 */
public class UserDAO implements IBaseDAO<User> {

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan ORDER BY MaTaiKhoan DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public User getById(int id) throws SQLException {
        String sql = "SELECT * FROM TaiKhoan WHERE MaTaiKhoan = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(User user) throws SQLException {
        String sql = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, VaiTro, MaNhanVien, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getTenDangNhap());
            ps.setString(2, user.getMatKhau());
            ps.setString(3, user.getVaiTro());
            ps.setInt(4, user.getMaNhanVien());
            ps.setString(5, user.getTrangThai());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setMaTaiKhoan(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(User user) throws SQLException {
        String sql = "UPDATE TaiKhoan SET TenDangNhap = ?, MatKhau = ?, VaiTro = ?, MaNhanVien = ?, TrangThai = ?, NgayCapNhat = GETDATE() " +
                     "WHERE MaTaiKhoan = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getTenDangNhap());
            ps.setString(2, user.getMatKhau());
            ps.setString(3, user.getVaiTro());
            ps.setInt(4, user.getMaNhanVien());
            ps.setString(5, user.getTrangThai());
            ps.setInt(6, user.getMaTaiKhoan());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM TaiKhoan WHERE MaTaiKhoan = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<User> search(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap LIKE ? OR VaiTro LIKE ? ORDER BY MaTaiKhoan DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        }
        return users;
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM TaiKhoan";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Đăng nhập - tìm user theo username và password
     */
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ? AND TrangThai = N'Hoạt động'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE TenDangNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Đổi mật khẩu
     */
    public boolean changePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE TaiKhoan SET MatKhau = ?, NgayCapNhat = GETDATE() WHERE MaTaiKhoan = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setMaTaiKhoan(rs.getInt("MaTaiKhoan"));
        user.setTenDangNhap(rs.getString("TenDangNhap"));
        user.setMatKhau(rs.getString("MatKhau"));
        user.setVaiTro(rs.getString("VaiTro"));
        user.setMaNhanVien(rs.getInt("MaNhanVien"));
        user.setTrangThai(rs.getString("TrangThai"));

        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            user.setNgayTao(ngayTao);
        }

        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            user.setNgayCapNhat(ngayCapNhat);
        }

        return user;
    }
}
