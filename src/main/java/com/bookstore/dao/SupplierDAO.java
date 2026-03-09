package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Supplier DAO - Data Access Object for Supplier entity
 */
public class SupplierDAO implements IBaseDAO<Supplier> {

    @Override
    public List<Supplier> getAll() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap ORDER BY MaNhaCungCap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                suppliers.add(mapResultSetToSupplier(rs));
            }
        }
        return suppliers;
    }

    @Override
    public Supplier getById(int id) throws SQLException {
        String sql = "SELECT * FROM NhaCungCap WHERE MaNhaCungCap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSupplier(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO NhaCungCap (TenNhaCungCap, DiaChi, SoDienThoai, Email, NguoiLienHe, MoTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, supplier.getTenNhaCungCap());
            ps.setString(2, supplier.getDiaChi());
            ps.setString(3, supplier.getSoDienThoai());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getNguoiLienHe());
            ps.setString(6, supplier.getMoTa());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        supplier.setMaNhaCungCap(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE NhaCungCap SET TenNhaCungCap = ?, DiaChi = ?, SoDienThoai = ?, " +
                     "Email = ?, NguoiLienHe = ?, MoTa = ?, NgayCapNhat = GETDATE() WHERE MaNhaCungCap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplier.getTenNhaCungCap());
            ps.setString(2, supplier.getDiaChi());
            ps.setString(3, supplier.getSoDienThoai());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getNguoiLienHe());
            ps.setString(6, supplier.getMoTa());
            ps.setInt(7, supplier.getMaNhaCungCap());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM NhaCungCap WHERE MaNhaCungCap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Supplier> search(String keyword) throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE TenNhaCungCap LIKE ? OR SoDienThoai LIKE ? OR Email LIKE ? ORDER BY MaNhaCungCap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    suppliers.add(mapResultSetToSupplier(rs));
                }
            }
        }
        return suppliers;
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhaCungCap";

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
     * Map ResultSet to Supplier object
     */
    private Supplier mapResultSetToSupplier(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setMaNhaCungCap(rs.getInt("MaNhaCungCap"));
        supplier.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
        supplier.setDiaChi(rs.getString("DiaChi"));
        supplier.setSoDienThoai(rs.getString("SoDienThoai"));
        supplier.setEmail(rs.getString("Email"));
        supplier.setNguoiLienHe(rs.getString("NguoiLienHe"));
        supplier.setMoTa(rs.getString("MoTa"));

        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            supplier.setNgayTao(ngayTao);
        }

        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            supplier.setNgayCapNhat(ngayCapNhat);
        }

        return supplier;
    }
}
