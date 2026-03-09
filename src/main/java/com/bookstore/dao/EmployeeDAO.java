package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee DAO - Data Access Object for Employee entity
 */
public class EmployeeDAO implements IBaseDAO<Employee> {

    @Override
    public List<Employee> getAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY MaNhanVien DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        }
        return employees;
    }

    @Override
    public Employee getById(int id) throws SQLException {
        String sql = "SELECT * FROM NhanVien WHERE MaNhanVien = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(Employee employee) throws SQLException {
        String sql = "INSERT INTO NhanVien (HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, DiaChi, ChucVu, Luong, NgayVaoLam, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, employee.getHoTen());
            ps.setString(2, employee.getGioiTinh());
            ps.setDate(3, employee.getNgaySinh() != null ? new Date(employee.getNgaySinh().getTime()) : null);
            ps.setString(4, employee.getSoDienThoai());
            ps.setString(5, employee.getEmail());
            ps.setString(6, employee.getDiaChi());
            ps.setString(7, employee.getChucVu());
            ps.setDouble(8, employee.getLuong());
            ps.setDate(9, employee.getNgayVaoLam() != null ? new Date(employee.getNgayVaoLam().getTime()) : null);
            ps.setString(10, employee.getTrangThai());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setMaNhanVien(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE NhanVien SET HoTen = ?, GioiTinh = ?, NgaySinh = ?, SoDienThoai = ?, " +
                     "Email = ?, DiaChi = ?, ChucVu = ?, Luong = ?, NgayVaoLam = ?, TrangThai = ?, NgayCapNhat = GETDATE() " +
                     "WHERE MaNhanVien = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.getHoTen());
            ps.setString(2, employee.getGioiTinh());
            ps.setDate(3, employee.getNgaySinh() != null ? new Date(employee.getNgaySinh().getTime()) : null);
            ps.setString(4, employee.getSoDienThoai());
            ps.setString(5, employee.getEmail());
            ps.setString(6, employee.getDiaChi());
            ps.setString(7, employee.getChucVu());
            ps.setDouble(8, employee.getLuong());
            ps.setDate(9, employee.getNgayVaoLam() != null ? new Date(employee.getNgayVaoLam().getTime()) : null);
            ps.setString(10, employee.getTrangThai());
            ps.setInt(11, employee.getMaNhanVien());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM NhanVien WHERE MaNhanVien = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Employee> search(String keyword) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE HoTen LIKE ? OR SoDienThoai LIKE ? OR Email LIKE ? ORDER BY MaNhanVien DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
        }
        return employees;
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhanVien";

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
     * Lấy nhân viên theo chức vụ
     */
    public List<Employee> getByPosition(String position) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE ChucVu = ? ORDER BY HoTen";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, position);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
        }
        return employees;
    }

    /**
     * Map ResultSet to Employee object
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setMaNhanVien(rs.getInt("MaNhanVien"));
        employee.setHoTen(rs.getString("HoTen"));
        employee.setGioiTinh(rs.getString("GioiTinh"));

        Date ngaySinh = rs.getDate("NgaySinh");
        if (ngaySinh != null) {
            employee.setNgaySinh(ngaySinh);
        }

        employee.setSoDienThoai(rs.getString("SoDienThoai"));
        employee.setEmail(rs.getString("Email"));
        employee.setDiaChi(rs.getString("DiaChi"));
        employee.setChucVu(rs.getString("ChucVu"));
        employee.setLuong(rs.getDouble("Luong"));

        Date ngayVaoLam = rs.getDate("NgayVaoLam");
        if (ngayVaoLam != null) {
            employee.setNgayVaoLam(ngayVaoLam);
        }

        employee.setTrangThai(rs.getString("TrangThai"));

        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            employee.setNgayTao(ngayTao);
        }

        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            employee.setNgayCapNhat(ngayCapNhat);
        }

        return employee;
    }
}
