package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Invoice DAO - Data Access Object for Invoice entity
 */
public class InvoiceDAO implements IBaseDAO<Invoice> {

    @Override
    public List<Invoice> getAll() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY MaHoaDon DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }
        return invoices;
    }

    @Override
    public Invoice getById(int id) throws SQLException {
        String sql = "SELECT * FROM HoaDon WHERE MaHoaDon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO HoaDon (MaHoaDonString, MaKhachHang, MaNhanVien, NgayLap, TongTien, GiamGia, ThanhToan, PhuongThucThanhToan, GhiChu, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, invoice.getMaHoaDonString());
            ps.setInt(2, invoice.getMaKhachHang());
            ps.setInt(3, invoice.getMaNhanVien());
            ps.setTimestamp(4, invoice.getNgayLap() != null ? new Timestamp(invoice.getNgayLap().getTime()) : new Timestamp(System.currentTimeMillis()));
            ps.setDouble(5, invoice.getTongTien());
            ps.setDouble(6, invoice.getGiamGia());
            ps.setDouble(7, invoice.getThanhToan());
            ps.setString(8, invoice.getPhuongThucThanhToan());
            ps.setString(9, invoice.getGhiChu());
            ps.setString(10, invoice.getTrangThai());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        invoice.setMaHoaDon(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Invoice invoice) throws SQLException {
        String sql = "UPDATE HoaDon SET MaHoaDonString = ?, MaKhachHang = ?, MaNhanVien = ?, NgayLap = ?, " +
                     "TongTien = ?, GiamGia = ?, ThanhToan = ?, PhuongThucThanhToan = ?, GhiChu = ?, TrangThai = ?, NgayCapNhat = GETDATE() " +
                     "WHERE MaHoaDon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoice.getMaHoaDonString());
            ps.setInt(2, invoice.getMaKhachHang());
            ps.setInt(3, invoice.getMaNhanVien());
            ps.setTimestamp(4, invoice.getNgayLap() != null ? new Timestamp(invoice.getNgayLap().getTime()) : new Timestamp(System.currentTimeMillis()));
            ps.setDouble(5, invoice.getTongTien());
            ps.setDouble(6, invoice.getGiamGia());
            ps.setDouble(7, invoice.getThanhToan());
            ps.setString(8, invoice.getPhuongThucThanhToan());
            ps.setString(9, invoice.getGhiChu());
            ps.setString(10, invoice.getTrangThai());
            ps.setInt(11, invoice.getMaHoaDon());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM HoaDon WHERE MaHoaDon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Invoice> search(String keyword) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaHoaDonString LIKE ? OR GhiChu LIKE ? ORDER BY MaHoaDon DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }
        return invoices;
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon";

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
     * Lấy hóa đơn theo ngày
     */
    public List<Invoice> getByDateRange(Date startDate, Date endDate) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE NgayLap BETWEEN ? AND ? ORDER BY NgayLap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }
        return invoices;
    }

    /**
     * Lấy hóa đơn theo khách hàng
     */
    public List<Invoice> getByCustomer(int customerId) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaKhachHang = ? ORDER BY NgayLap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }
        return invoices;
    }

    /**
     * Tính tổng doanh thu theo ngày
     */
    public double getRevenueByDate(Date date) throws SQLException {
        String sql = "SELECT ISNULL(SUM(ThanhToan), 0) FROM HoaDon WHERE CAST(NgayLap AS DATE) = CAST(? AS DATE) AND TrangThai = N'Đã thanh toán'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(date.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0;
    }

    /**
     * Tính tổng doanh thu trong khoảng thời gian
     */
    public double getRevenueByDateRange(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT ISNULL(SUM(ThanhToan), 0) FROM HoaDon WHERE NgayLap BETWEEN ? AND ? AND TrangThai = N'Đã thanh toán'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0;
    }

    /**
     * Tạo mã hóa đơn tự động
     */
    public String generateInvoiceCode() throws SQLException {
        String sql = "SELECT TOP 1 MaHoaDonString FROM HoaDon ORDER BY MaHoaDon DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastCode = rs.getString("MaHoaDonString");
                int number = Integer.parseInt(lastCode.substring(2));
                return String.format("HD%05d", number + 1);
            }
        }
        return "HD00001";
    }

    /**
     * Map ResultSet to Invoice object
     */
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setMaHoaDon(rs.getInt("MaHoaDon"));
        invoice.setMaHoaDonString(rs.getString("MaHoaDonString"));
        invoice.setMaKhachHang(rs.getInt("MaKhachHang"));
        invoice.setMaNhanVien(rs.getInt("MaNhanVien"));

        Timestamp ngayLap = rs.getTimestamp("NgayLap");
        if (ngayLap != null) {
            invoice.setNgayLap(ngayLap);
        }

        invoice.setTongTien(rs.getDouble("TongTien"));
        invoice.setGiamGia(rs.getDouble("GiamGia"));
        invoice.setThanhToan(rs.getDouble("ThanhToan"));
        invoice.setPhuongThucThanhToan(rs.getString("PhuongThucThanhToan"));
        invoice.setGhiChu(rs.getString("GhiChu"));
        invoice.setTrangThai(rs.getString("TrangThai"));

        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            invoice.setNgayTao(ngayTao);
        }

        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            invoice.setNgayCapNhat(ngayCapNhat);
        }

        return invoice;
    }
}
