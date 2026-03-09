package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.ImportOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ImportOrder DAO - Data Access Object for ImportOrder entity
 */
public class ImportOrderDAO implements IBaseDAO<ImportOrder> {

    @Override
    public List<ImportOrder> getAll() throws SQLException {
        List<ImportOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM DonNhapHang ORDER BY MaDonNhap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(mapResultSetToImportOrder(rs));
            }
        }
        return orders;
    }

    @Override
    public ImportOrder getById(int id) throws SQLException {
        String sql = "SELECT * FROM DonNhapHang WHERE MaDonNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToImportOrder(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(ImportOrder order) throws SQLException {
        String sql = "INSERT INTO DonNhapHang (MaDonNhapString, MaNhaCungCap, MaNhanVien, NgayNhap, TongTien, GhiChu, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, order.getMaDonNhapString());
            ps.setInt(2, order.getMaNhaCungCap());
            ps.setInt(3, order.getMaNhanVien());
            ps.setTimestamp(4, order.getNgayNhap() != null ? new Timestamp(order.getNgayNhap().getTime()) : new Timestamp(System.currentTimeMillis()));
            ps.setDouble(5, order.getTongTien());
            ps.setString(6, order.getGhiChu());
            ps.setString(7, order.getTrangThai());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setMaDonNhap(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(ImportOrder order) throws SQLException {
        String sql = "UPDATE DonNhapHang SET MaDonNhapString = ?, MaNhaCungCap = ?, MaNhanVien = ?, NgayNhap = ?, " +
                     "TongTien = ?, GhiChu = ?, TrangThai = ?, NgayCapNhat = GETDATE() WHERE MaDonNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getMaDonNhapString());
            ps.setInt(2, order.getMaNhaCungCap());
            ps.setInt(3, order.getMaNhanVien());
            ps.setTimestamp(4, order.getNgayNhap() != null ? new Timestamp(order.getNgayNhap().getTime()) : new Timestamp(System.currentTimeMillis()));
            ps.setDouble(5, order.getTongTien());
            ps.setString(6, order.getGhiChu());
            ps.setString(7, order.getTrangThai());
            ps.setInt(8, order.getMaDonNhap());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM DonNhapHang WHERE MaDonNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<ImportOrder> search(String keyword) throws SQLException {
        List<ImportOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM DonNhapHang WHERE MaDonNhapString LIKE ? OR GhiChu LIKE ? ORDER BY MaDonNhap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToImportOrder(rs));
                }
            }
        }
        return orders;
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM DonNhapHang";

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
     * Tạo mã đơn nhập tự động
     */
    public String generateImportCode() throws SQLException {
        String sql = "SELECT TOP 1 MaDonNhapString FROM DonNhapHang ORDER BY MaDonNhap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastCode = rs.getString("MaDonNhapString");
                int number = Integer.parseInt(lastCode.substring(2));
                return String.format("PN%05d", number + 1);
            }
        }
        return "PN00001";
    }

    /**
     * Map ResultSet to ImportOrder object
     */
    private ImportOrder mapResultSetToImportOrder(ResultSet rs) throws SQLException {
        ImportOrder order = new ImportOrder();
        order.setMaDonNhap(rs.getInt("MaDonNhap"));
        order.setMaDonNhapString(rs.getString("MaDonNhapString"));
        order.setMaNhaCungCap(rs.getInt("MaNhaCungCap"));
        order.setMaNhanVien(rs.getInt("MaNhanVien"));

        Timestamp ngayNhap = rs.getTimestamp("NgayNhap");
        if (ngayNhap != null) {
            order.setNgayNhap(ngayNhap);
        }

        order.setTongTien(rs.getDouble("TongTien"));
        order.setGhiChu(rs.getString("GhiChu"));
        order.setTrangThai(rs.getString("TrangThai"));

        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            order.setNgayTao(ngayTao);
        }

        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            order.setNgayCapNhat(ngayCapNhat);
        }

        return order;
    }
}
