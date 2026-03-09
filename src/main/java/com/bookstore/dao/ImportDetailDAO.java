package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.ImportDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ImportDetail DAO - Data Access Object for ImportDetail entity
 */
public class ImportDetailDAO implements IBaseDAO<ImportDetail> {

    @Override
    public List<ImportDetail> getAll() throws SQLException {
        List<ImportDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietDonNhap";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                details.add(mapResultSetToImportDetail(rs));
            }
        }
        return details;
    }

    @Override
    public ImportDetail getById(int id) throws SQLException {
        String sql = "SELECT * FROM ChiTietDonNhap WHERE MaChiTiet = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToImportDetail(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(ImportDetail detail) throws SQLException {
        String sql = "INSERT INTO ChiTietDonNhap (MaDonNhap, MaSach, SoLuong, DonGia, ThanhTien) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detail.getMaDonNhap());
            ps.setInt(2, detail.getMaSach());
            ps.setInt(3, detail.getSoLuong());
            ps.setDouble(4, detail.getDonGia());
            ps.setDouble(5, detail.getThanhTien());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        detail.setMaChiTiet(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(ImportDetail detail) throws SQLException {
        String sql = "UPDATE ChiTietDonNhap SET MaDonNhap = ?, MaSach = ?, SoLuong = ?, " +
                     "DonGia = ?, ThanhTien = ? WHERE MaChiTiet = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getMaDonNhap());
            ps.setInt(2, detail.getMaSach());
            ps.setInt(3, detail.getSoLuong());
            ps.setDouble(4, detail.getDonGia());
            ps.setDouble(5, detail.getThanhTien());
            ps.setInt(6, detail.getMaChiTiet());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM ChiTietDonNhap WHERE MaChiTiet = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<ImportDetail> search(String keyword) throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietDonNhap";

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
     * Lấy chi tiết đơn nhập theo mã đơn
     */
    public List<ImportDetail> getByImportId(int importId) throws SQLException {
        List<ImportDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietDonNhap WHERE MaDonNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, importId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    details.add(mapResultSetToImportDetail(rs));
                }
            }
        }
        return details;
    }

    /**
     * Xóa chi tiết theo mã đơn nhập
     */
    public boolean deleteByImportId(int importId) throws SQLException {
        String sql = "DELETE FROM ChiTietDonNhap WHERE MaDonNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, importId);
            return ps.executeUpdate() >= 0;
        }
    }

    /**
     * Map ResultSet to ImportDetail object
     */
    private ImportDetail mapResultSetToImportDetail(ResultSet rs) throws SQLException {
        ImportDetail detail = new ImportDetail();
        detail.setMaChiTiet(rs.getInt("MaChiTiet"));
        detail.setMaDonNhap(rs.getInt("MaDonNhap"));
        detail.setMaSach(rs.getInt("MaSach"));
        detail.setSoLuong(rs.getInt("SoLuong"));
        detail.setDonGia(rs.getDouble("DonGia"));
        detail.setThanhTien(rs.getDouble("ThanhTien"));
        return detail;
    }
}
