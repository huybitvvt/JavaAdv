package com.bookstore.service;

import com.bookstore.dao.ImportOrderDAO;
import com.bookstore.dao.ImportDetailDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.model.ImportOrder;
import com.bookstore.model.ImportDetail;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Import Service - Business logic layer for Import Orders
 */
public class ImportService {

    private final ImportOrderDAO importOrderDAO;
    private final ImportDetailDAO importDetailDAO;
    private final BookDAO bookDAO;

    public ImportService() {
        this.importOrderDAO = new ImportOrderDAO();
        this.importDetailDAO = new ImportDetailDAO();
        this.bookDAO = new BookDAO();
    }

    public List<ImportOrder> getAllImportOrders() {
        try {
            return importOrderDAO.getAll();
        } catch (SQLException e) {
            System.err.println("Error getting all import orders: " + e.getMessage());
            return null;
        }
    }

    public ImportOrder getImportOrderById(int id) {
        try {
            return importOrderDAO.getById(id);
        } catch (SQLException e) {
            System.err.println("Error getting import order by id: " + e.getMessage());
            return null;
        }
    }

    public boolean createImportOrder(ImportOrder order, List<ImportDetail> details) {
        try {
            // Generate import code
            String importCode = importOrderDAO.generateImportCode();
            order.setMaDonNhapString(importCode);
            order.setNgayNhap(new Date());
            order.setTrangThai("Đã nhập");

            // Insert import order
            boolean success = importOrderDAO.insert(order);
            if (!success) {
                return false;
            }

            // Insert import details and update book quantity
            for (ImportDetail detail : details) {
                detail.setMaDonNhap(order.getMaDonNhap());
                detail.tinhThanhTien();

                // Insert detail
                success = importDetailDAO.insert(detail);
                if (!success) {
                    return false;
                }

                // Update book quantity (increase)
                bookDAO.updateQuantity(detail.getMaSach(), detail.getSoLuong());
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error creating import order: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteImportOrder(int id) {
        try {
            // Get import details first
            List<ImportDetail> details = importDetailDAO.getByImportId(id);

            // Restore book quantities (decrease)
            for (ImportDetail detail : details) {
                bookDAO.updateQuantity(detail.getMaSach(), -detail.getSoLuong());
            }

            // Delete import details
            importDetailDAO.deleteByImportId(id);

            // Delete import order
            return importOrderDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error deleting import order: " + e.getMessage());
            return false;
        }
    }

    public List<ImportDetail> getImportDetails(int importId) {
        try {
            return importDetailDAO.getByImportId(importId);
        } catch (SQLException e) {
            System.err.println("Error getting import details: " + e.getMessage());
            return null;
        }
    }

    public String generateImportCode() {
        try {
            return importOrderDAO.generateImportCode();
        } catch (SQLException e) {
            System.err.println("Error generating import code: " + e.getMessage());
            return "PN00001";
        }
    }

    public List<ImportOrder> searchImportOrders(String keyword) {
        try {
            return importOrderDAO.search(keyword);
        } catch (SQLException e) {
            System.err.println("Error searching import orders: " + e.getMessage());
            return null;
        }
    }
}
