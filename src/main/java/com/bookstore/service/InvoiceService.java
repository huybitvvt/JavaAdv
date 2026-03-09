package com.bookstore.service;

import com.bookstore.dao.InvoiceDAO;
import com.bookstore.dao.InvoiceDetailDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CustomerDAO;
import com.bookstore.model.Invoice;
import com.bookstore.model.InvoiceDetail;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Invoice Service - Business logic layer for Invoice
 */
public class InvoiceService {

    private final InvoiceDAO invoiceDAO;
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final BookDAO bookDAO;
    private final CustomerDAO customerDAO;

    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAO();
        this.invoiceDetailDAO = new InvoiceDetailDAO();
        this.bookDAO = new BookDAO();
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Lấy tất cả hóa đơn
     */
    public List<Invoice> getAllInvoices() {
        try {
            return invoiceDAO.getAll();
        } catch (SQLException e) {
            System.err.println("Error getting all invoices: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy hóa đơn theo ID
     */
    public Invoice getInvoiceById(int id) {
        try {
            Invoice invoice = invoiceDAO.getById(id);
            if (invoice != null) {
                loadInvoiceDetails(invoice);
            }
            return invoice;
        } catch (SQLException e) {
            System.err.println("Error getting invoice by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tạo hóa đơn mới (có transaction)
     */
    public boolean createInvoice(Invoice invoice, List<InvoiceDetail> details) {
        try {
            // Generate invoice code
            String invoiceCode = invoiceDAO.generateInvoiceCode();
            invoice.setMaHoaDonString(invoiceCode);
            invoice.setNgayLap(new Date());
            invoice.setTrangThai("Đã thanh toán");

            // Insert invoice
            boolean success = invoiceDAO.insert(invoice);
            if (!success) {
                return false;
            }

            // Insert invoice details and update book quantity
            for (InvoiceDetail detail : details) {
                detail.setMaHoaDon(invoice.getMaHoaDon());
                detail.tinhThanhTien();

                // Insert detail
                success = invoiceDetailDAO.insert(detail);
                if (!success) {
                    return false;
                }

                // Update book quantity (decrease)
                bookDAO.updateQuantity(detail.getMaSach(), -detail.getSoLuong());
            }

            // Update customer points (1% of total)
            if (invoice.getMaKhachHang() > 0) {
                int points = (int) (invoice.getThanhToan() / 1000); // 1 point per 1000 VND
                customerDAO.updatePoints(invoice.getMaKhachHang(), points);
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error creating invoice: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật hóa đơn
     */
    public boolean updateInvoice(Invoice invoice) {
        try {
            return invoiceDAO.update(invoice);
        } catch (SQLException e) {
            System.err.println("Error updating invoice: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa hóa đơn (hoàn lại số lượng sách)
     */
    public boolean deleteInvoice(int id) {
        try {
            // Get invoice details first
            List<InvoiceDetail> details = invoiceDetailDAO.getByInvoiceId(id);

            // Restore book quantities
            for (InvoiceDetail detail : details) {
                bookDAO.updateQuantity(detail.getMaSach(), detail.getSoLuong());
            }

            // Delete invoice details
            invoiceDetailDAO.deleteByInvoiceId(id);

            // Delete invoice
            return invoiceDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error deleting invoice: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tìm kiếm hóa đơn
     */
    public List<Invoice> searchInvoices(String keyword) {
        try {
            return invoiceDAO.search(keyword);
        } catch (SQLException e) {
            System.err.println("Error searching invoices: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy hóa đơn theo ngày
     */
    public List<Invoice> getInvoicesByDateRange(Date startDate, Date endDate) {
        try {
            return invoiceDAO.getByDateRange(startDate, endDate);
        } catch (SQLException e) {
            System.err.println("Error getting invoices by date: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy hóa đơn theo khách hàng
     */
    public List<Invoice> getInvoicesByCustomer(int customerId) {
        try {
            return invoiceDAO.getByCustomer(customerId);
        } catch (SQLException e) {
            System.err.println("Error getting invoices by customer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy chi tiết hóa đơn
     */
    public List<InvoiceDetail> getInvoiceDetails(int invoiceId) {
        try {
            return invoiceDetailDAO.getByInvoiceId(invoiceId);
        } catch (SQLException e) {
            System.err.println("Error getting invoice details: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tính doanh thu theo ngày
     */
    public double getRevenueByDate(Date date) {
        try {
            return invoiceDAO.getRevenueByDate(date);
        } catch (SQLException e) {
            System.err.println("Error getting revenue by date: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Tính doanh thu trong khoảng thời gian
     */
    public double getRevenueByDateRange(Date startDate, Date endDate) {
        try {
            return invoiceDAO.getRevenueByDateRange(startDate, endDate);
        } catch (SQLException e) {
            System.err.println("Error getting revenue by date range: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Tạo mã hóa đơn mới
     */
    public String generateInvoiceCode() {
        try {
            return invoiceDAO.generateInvoiceCode();
        } catch (SQLException e) {
            System.err.println("Error generating invoice code: " + e.getMessage());
            return "HD00001";
        }
    }

    /**
     * Load chi tiết và thông tin liên quan cho hóa đơn
     */
    private void loadInvoiceDetails(Invoice invoice) {
        try {
            // Load invoice details
            List<InvoiceDetail> details = invoiceDetailDAO.getByInvoiceId(invoice.getMaHoaDon());
            invoice.setChiTietHoaDon(details);

            // Load customer
            if (invoice.getMaKhachHang() > 0) {
                invoice.setKhachHang(customerDAO.getById(invoice.getMaKhachHang()));
            }
        } catch (SQLException e) {
            System.err.println("Error loading invoice details: " + e.getMessage());
        }
    }

    /**
     * Đếm tổng số hóa đơn
     */
    public int countInvoices() {
        try {
            return invoiceDAO.count();
        } catch (SQLException e) {
            System.err.println("Error counting invoices: " + e.getMessage());
            return 0;
        }
    }
}
