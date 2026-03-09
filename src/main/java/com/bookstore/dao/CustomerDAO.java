package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer DAO - Data Access Object for Customer entity
 */
public class CustomerDAO implements IBaseDAO<Customer> {

    @Override
    public List<Customer> getAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY MaKhachHang DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    @Override
    public Customer getById(int id) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE MaKhachHang = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO KhachHang (HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, DiaChi, DiemTichLuy) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getHoTen());
            ps.setString(2, customer.getGioiTinh());
            ps.setDate(3, customer.getNgaySinh() != null ? new Date(customer.getNgaySinh().getTime()) : null);
            ps.setString(4, customer.getSoDienThoai());
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getDiaChi());
            ps.setInt(7, customer.getDiemTichLuy());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        customer.setMaKhachHang(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE KhachHang SET HoTen = ?, GioiTinh = ?, NgaySinh = ?, SoDienThoai = ?, " +
                     "Email = ?, DiaChi = ?, DiemTichLuy = ?, NgayCapNhat = GETDATE() WHERE MaKhachHang = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getHoTen());
            ps.setString(2, customer.getGioiTinh());
            ps.setDate(3, customer.getNgaySinh() != null ? new Date(customer.getNgaySinh().getTime()) : null);
            ps.setString(4, customer.getSoDienThoai());
            ps.setString(5, customer.getEmail());
            ps.setString(6, customer.getDiaChi());
            ps.setInt(7, customer.getDiemTichLuy());
            ps.setInt(8, customer.getMaKhachHang());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE MaKhachHang = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Customer> search(String keyword) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE HoTen LIKE ? OR SoDienThoai LIKE ? OR Email LIKE ? ORDER BY MaKhachHang DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }
        return customers;
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM KhachHang";

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
     * Cập nhật điểm tích lũy
     */
    public boolean updatePoints(int customerId, int points) throws SQLException {
        String sql = "UPDATE KhachHang SET DiemTichLuy = DiemTichLuy + ?, NgayCapNhat = GETDATE() WHERE MaKhachHang = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, points);
            ps.setInt(2, customerId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm khách hàng theo số điện thoại
     */
    public Customer getByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    /**
     * Lấy top khách hàng tích lũy nhiều nhất
     */
    public List<Customer> getTopCustomers(int limit) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM KhachHang ORDER BY DiemTichLuy DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }
        return customers;
    }

    /**
     * Map ResultSet to Customer object
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setMaKhachHang(rs.getInt("MaKhachHang"));
        customer.setHoTen(rs.getString("HoTen"));
        customer.setGioiTinh(rs.getString("GioiTinh"));

        Date ngaySinh = rs.getDate("NgaySinh");
        if (ngaySinh != null) {
            customer.setNgaySinh(ngaySinh);
        }

        customer.setSoDienThoai(rs.getString("SoDienThoai"));
        customer.setEmail(rs.getString("Email"));
        customer.setDiaChi(rs.getString("DiaChi"));
        customer.setDiemTichLuy(rs.getInt("DiemTichLuy"));

        Timestamp ngayDangKy = rs.getTimestamp("NgayDangKy");
        if (ngayDangKy != null) {
            customer.setNgayDangKy(ngayDangKy);
        }

        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            customer.setNgayCapNhat(ngayCapNhat);
        }

        return customer;
    }
}
