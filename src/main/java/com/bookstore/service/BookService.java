package com.bookstore.service;

import com.bookstore.dao.BookDAO;
import com.bookstore.exception.BusinessException;
import com.bookstore.model.Book;

import java.sql.SQLException;
import java.util.List;

/**
 * Book Service - Business logic layer for Book
 */
public class BookService {

    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    /**
     * Lấy tất cả sách
     */
    public List<Book> getAllBooks() {
        try {
            return bookDAO.getAll();
        } catch (SQLException e) {
            System.err.println("[ERROR] Error getting all books: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể lấy danh sách sách. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Lấy sách theo ID
     */
    public Book getBookById(int id) {
        try {
            return bookDAO.getById(id);
        } catch (SQLException e) {
            System.err.println("[ERROR] Error getting book by id " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể lấy thông tin sách. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Thêm sách mới
     */
    public boolean addBook(Book book) {
        try {
            // Validation
            if (book.getTenSach() == null || book.getTenSach().trim().isEmpty()) {
                System.err.println("[WARN] Book name is required");
                throw new BusinessException("Tên sách không được để trống");
            }
            if (book.getGiaBia() < 0) {
                System.err.println("[WARN] Price cannot be negative");
                throw new BusinessException("Giá bìa không được âm");
            }
            if (book.getSoLuongTon() < 0) {
                System.err.println("[WARN] Quantity cannot be negative");
                throw new BusinessException("Số lượng tồn không được âm");
            }
            if (book.getNamXuatBan() < 1900 || book.getNamXuatBan() > 2100) {
                System.err.println("[WARN] Invalid publication year: " + book.getNamXuatBan());
                throw new BusinessException("Năm xuất bản không hợp lệ");
            }

            boolean result = bookDAO.insert(book);
            if (result) {
                System.out.println("[INFO] Book added successfully: " + book.getTenSach());
            }
            return result;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error adding book: " + e.getMessage());
            e.printStackTrace();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                throw new BusinessException("Dữ liệu sách đã tồn tại hoặc vi phạm ràng buộc cơ sở dữ liệu");
            }
            throw new RuntimeException("Không thể thêm sách. Vui lòng thử lại sau.", e);
        } catch (BusinessException e) {
            throw e; // Re-throw business exceptions
        }
    }

    /**
     * Cập nhật sách
     */
    public boolean updateBook(Book book) {
        try {
            // Validation
            if (book.getTenSach() == null || book.getTenSach().trim().isEmpty()) {
                System.err.println("[WARN] Book name is required for update");
                throw new BusinessException("Tên sách không được để trống");
            }
            if (book.getGiaBia() < 0) {
                System.err.println("[WARN] Price cannot be negative for update");
                throw new BusinessException("Giá bìa không được âm");
            }
            if (book.getSoLuongTon() < 0) {
                System.err.println("[WARN] Quantity cannot be negative for update");
                throw new BusinessException("Số lượng tồn không được âm");
            }

            boolean result = bookDAO.update(book);
            if (result) {
                System.out.println("[INFO] Book updated successfully: " + book.getMaSach());
            }
            return result;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error updating book: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể cập nhật sách. Vui lòng thử lại sau.", e);
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * Xóa sách
     */
    public boolean deleteBook(int id) {
        try {
            boolean result = bookDAO.delete(id);
            if (result) {
                System.out.println("[INFO] Book deleted successfully: " + id);
            } else {
                System.err.println("[WARN] Book not found for deletion: " + id);
            }
            return result;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error deleting book: " + e.getMessage());
            e.printStackTrace();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                throw new BusinessException("Không thể xóa sách vì có dữ liệu liên quan");
            }
            throw new RuntimeException("Không thể xóa sách. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Tìm Kiếm sách
     */
    public List<Book> searchBooks(String keyword) {
        try {
            return bookDAO.search(keyword);
        } catch (SQLException e) {
            System.err.println("[ERROR] Error searching books: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể tìm kiếm sách. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Lấy sách theo thể loại
     */
    public List<Book> getBooksByCategory(String category) {
        try {
            return bookDAO.getByCategory(category);
        } catch (SQLException e) {
            System.err.println("[ERROR] Error getting books by category: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể lấy sách theo thể loại. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Lấy sách tồn kho thấp
     */
    public List<Book> getLowStockBooks(int threshold) {
        try {
            return bookDAO.getLowStock(threshold);
        } catch (SQLException e) {
            System.err.println("[ERROR] Error getting low stock books: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể lấy sách tồn kho thấp. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Cập nhật số lượng tồn kho
     */
    public boolean updateQuantity(int bookId, int quantity) {
        try {
            boolean result = bookDAO.updateQuantity(bookId, quantity);
            if (result) {
                System.out.println("[INFO] Quantity updated for book " + bookId + ": " + quantity);
            }
            return result;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error updating quantity: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể cập nhật số lượng. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Lấy tất cả thể loại
     */
    public List<String> getAllCategories() {
        try {
            return bookDAO.getAllCategories();
        } catch (SQLException e) {
            System.err.println("[ERROR] Error getting categories: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể lấy danh mục. Vui lòng thử lại sau.", e);
        }
    }

    /**
     * Đếm tổng số sách
     */
    public int countBooks() {
        try {
            return bookDAO.count();
        } catch (SQLException e) {
            System.err.println("[ERROR] Error counting books: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể đếm số sách. Vui lòng thử lại sau.", e);
        }
    }
}
