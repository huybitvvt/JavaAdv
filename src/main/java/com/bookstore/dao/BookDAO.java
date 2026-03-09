package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Book DAO - Data Access Object for Book entity
 */
public class BookDAO implements IBaseDAO<Book> {

    @Override
    public List<Book> getAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Sach ORDER BY MaSach DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        }
        return books;
    }

    @Override
    public Book getById(int id) throws SQLException {
        String sql = "SELECT * FROM Sach WHERE MaSach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(Book book) throws SQLException {
        String sql = "INSERT INTO Sach (TenSach, TacGia, NhaXuatBan, TheLoai, NamXuatBan, GiaBia, SoLuongTon, MoTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTenSach());
            ps.setString(2, book.getTacGia());
            ps.setString(3, book.getNhaXuatBan());
            ps.setString(4, book.getTheLoai());
            ps.setInt(5, book.getNamXuatBan());
            ps.setDouble(6, book.getGiaBia());
            ps.setInt(7, book.getSoLuongTon());
            ps.setString(8, book.getMoTa());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        book.setMaSach(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Book book) throws SQLException {
        String sql = "UPDATE Sach SET TenSach = ?, TacGia = ?, NhaXuatBan = ?, TheLoai = ?, " +
                     "NamXuatBan = ?, GiaBia = ?, SoLuongTon = ?, MoTa = ?, NgayCapNhat = GETDATE() " +
                     "WHERE MaSach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTenSach());
            ps.setString(2, book.getTacGia());
            ps.setString(3, book.getNhaXuatBan());
            ps.setString(4, book.getTheLoai());
            ps.setInt(5, book.getNamXuatBan());
            ps.setDouble(6, book.getGiaBia());
            ps.setInt(7, book.getSoLuongTon());
            ps.setString(8, book.getMoTa());
            ps.setInt(9, book.getMaSach());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Sach WHERE MaSach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Book> search(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Sach WHERE TenSach LIKE ? OR TacGia LIKE ? OR TheLoai LIKE ? ORDER BY MaSach DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sach";

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
     * Tìm kiếm sách theo thể loại
     */
    public List<Book> getByCategory(String category) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Sach WHERE TheLoai = ? ORDER BY TenSach";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }

    /**
     * Lấy sách tồn kho thấp
     */
    public List<Book> getLowStock(int threshold) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Sach WHERE SoLuongTon <= ? ORDER BY SoLuongTon";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, threshold);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }

    /**
     * Cập nhật số lượng tồn kho
     */
    public boolean updateQuantity(int bookId, int quantity) throws SQLException {
        String sql = "UPDATE Sach SET SoLuongTon = SoLuongTon + ?, NgayCapNhat = GETDATE() WHERE MaSach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, bookId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy tất cả thể loại sách
     */
    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT TheLoai FROM Sach WHERE TheLoai IS NOT NULL ORDER BY TheLoai";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("TheLoai"));
            }
        }
        return categories;
    }

    /**
     * Map ResultSet to Book object
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setMaSach(rs.getInt("MaSach"));
        book.setTenSach(rs.getString("TenSach"));
        book.setTacGia(rs.getString("TacGia"));
        book.setNhaXuatBan(rs.getString("NhaXuatBan"));
        book.setTheLoai(rs.getString("TheLoai"));
        book.setNamXuatBan(rs.getInt("NamXuatBan"));
        book.setGiaBia(rs.getDouble("GiaBia"));
        book.setSoLuongTon(rs.getInt("SoLuongTon"));
        book.setMoTa(rs.getString("MoTa"));

        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            book.setNgayTao(ngayTao);
        }

        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            book.setNgayCapNhat(ngayCapNhat);
        }

        return book;
    }
}
