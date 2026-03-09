package com.bookstore.service;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.model.Customer;

import java.sql.SQLException;
import java.util.List;

/**
 * Customer Service - Business logic layer for Customer
 */
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Lấy tất cả khách hàng
     */
    public List<Customer> getAllCustomers() {
        try {
            return customerDAO.getAll();
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy khách hàng theo ID
     */
    public Customer getCustomerById(int id) {
        try {
            return customerDAO.getById(id);
        } catch (SQLException e) {
            System.err.println("Error getting customer by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Thêm khách hàng mới
     */
    public boolean addCustomer(Customer customer) {
        try {
            // Validation
            if (customer.getHoTen() == null || customer.getHoTen().trim().isEmpty()) {
                System.err.println("Customer name is required");
                return false;
            }
            if (customer.getSoDienThoai() == null || customer.getSoDienThoai().trim().isEmpty()) {
                System.err.println("Phone number is required");
                return false;
            }
            return customerDAO.insert(customer);
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật khách hàng
     */
    public boolean updateCustomer(Customer customer) {
        try {
            // Validation
            if (customer.getHoTen() == null || customer.getHoTen().trim().isEmpty()) {
                System.err.println("Customer name is required");
                return false;
            }
            return customerDAO.update(customer);
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa khách hàng
     */
    public boolean deleteCustomer(int id) {
        try {
            return customerDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tìm kiếm khách hàng
     */
    public List<Customer> searchCustomers(String keyword) {
        try {
            return customerDAO.search(keyword);
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cập nhật điểm tích lũy
     */
    public boolean updatePoints(int customerId, int points) {
        try {
            return customerDAO.updatePoints(customerId, points);
        } catch (SQLException e) {
            System.err.println("Error updating points: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tìm khách hàng theo số điện thoại
     */
    public Customer getCustomerByPhone(String phone) {
        try {
            return customerDAO.getByPhone(phone);
        } catch (SQLException e) {
            System.err.println("Error getting customer by phone: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy top khách hàng tích lũy nhiều nhất
     */
    public List<Customer> getTopCustomers(int limit) {
        try {
            return customerDAO.getTopCustomers(limit);
        } catch (SQLException e) {
            System.err.println("Error getting top customers: " + e.getMessage());
            return null;
        }
    }

    /**
     * Đếm tổng số khách hàng
     */
    public int countCustomers() {
        try {
            return customerDAO.count();
        } catch (SQLException e) {
            System.err.println("Error counting customers: " + e.getMessage());
            return 0;
        }
    }
}
