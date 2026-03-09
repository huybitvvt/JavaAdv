package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.InvoiceDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InvoiceDetail DAO - Data Access Object for InvoiceDetail entity
 */
public class InvoiceDetailDAO implements IBaseDAO<InvoiceDetail> {

    @Override
    public List<InvoiceDetail> getAll() throws SQLException {
        List<InvoiceDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                details.add(mapResultSetToInvoiceDetail(rs));
            }
        }
        return details;
    }

    @Override
    public InvoiceDetail getById(int id) throws SQLException {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaChiTiet = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoiceDetail(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(InvoiceDetail detail) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (MaHoaDon, MaSach, SoLuong, DonGia, ThanhTien, GiamGia) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detail.getMaHoaDon());
            ps.setInt(2, detail.getMaSach());
            ps.setInt(3, detail.getSoLuong());
            ps.setDouble(4, detail.getDonGia());
            ps.setDouble(5, detail.getThanhTien());
            ps.setDouble(6, detail.getGiamGia());

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
    public boolean update(InvoiceDetail detail) throws SQLException {
        String sql = "UPDATE ChiTietHoaDon SET MaHoaDon = ?, MaSach = ?, SoLuong = ?, " +
                     "DonGia = ?, ThanhTien = ?, GiamGia = ? WHERE MaChiTiet = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getMaHoaDon());
            ps.setInt(2, detail.getMaSach());
            ps.setInt(3, detail.getSoLuong());
            ps.setDouble(4, detail.getDonGia());
            ps.setDouble(5, detail.getThanhTien());
            ps.setDouble(6, detail.getGiamGia());
            ps.setInt(7, detail.getMaChiTiet());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaChiTiet = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<InvoiceDetail> search(String keyword) throws SQLException {
        // Not implemented for details
        return new ArrayList<>();
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietHoaDon";

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
     * Lấy chi tiết hóa đơn theo mã hóa đơn
     */
    public List<InvoiceDetail> getByInvoiceId(int invoiceId) throws SQLException {
        List<InvoiceDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHoaDon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    details.add(mapResultSetToInvoiceDetail(rs));
                }
            }
        }
        return details;
    }

    /**
     * Xóa tất cả chi tiết theo mã hóa đơn
     */
    public boolean deleteByInvoiceId(int invoiceId) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHoaDon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);
            return ps.executeUpdate() >= 0;
        }
    }

    /**
     * Map ResultSet to InvoiceDetail object
     */
    private InvoiceDetail mapResultSetToInvoiceDetail(ResultSet rs) throws SQLException {
        InvoiceDetail detail = new InvoiceDetail();
        detail.setMaChiTiet(rs.getInt("MaChiTiet"));
        detail.setMaHoaDon(rs.getInt("MaHoaDon"));
        detail.setMaSach(rs.getInt("MaSach"));
        detail.setSoLuong(rs.getInt("SoLuong"));
        detail.setDonGia(rs.getDouble("DonGia"));
        detail.setThanhTien(rs.getDouble("ThanhTien"));
        detail.setGiamGia(rs.getDouble("GiamGia"));
        return detail;
    }
}
