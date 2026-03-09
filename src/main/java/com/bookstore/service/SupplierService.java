package com.bookstore.service;

import com.bookstore.dao.SupplierDAO;
import com.bookstore.model.Supplier;

import java.sql.SQLException;
import java.util.List;

/**
 * Supplier Service - Business logic layer for Supplier
 */
public class SupplierService {

    private final SupplierDAO supplierDAO;

    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    public List<Supplier> getAllSuppliers() {
        try {
            return supplierDAO.getAll();
        } catch (SQLException e) {
            System.err.println("Error getting all suppliers: " + e.getMessage());
            return null;
        }
    }

    public Supplier getSupplierById(int id) {
        try {
            return supplierDAO.getById(id);
        } catch (SQLException e) {
            System.err.println("Error getting supplier by id: " + e.getMessage());
            return null;
        }
    }

    public boolean addSupplier(Supplier supplier) {
        try {
            if (supplier.getTenNhaCungCap() == null || supplier.getTenNhaCungCap().trim().isEmpty()) {
                System.err.println("Supplier name is required");
                return false;
            }
            return supplierDAO.insert(supplier);
        } catch (SQLException e) {
            System.err.println("Error adding supplier: " + e.getMessage());
            return false;
        }
    }

    public boolean updateSupplier(Supplier supplier) {
        try {
            if (supplier.getTenNhaCungCap() == null || supplier.getTenNhaCungCap().trim().isEmpty()) {
                return false;
            }
            return supplierDAO.update(supplier);
        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSupplier(int id) {
        try {
            return supplierDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return false;
        }
    }

    public List<Supplier> searchSuppliers(String keyword) {
        try {
            return supplierDAO.search(keyword);
        } catch (SQLException e) {
            System.err.println("Error searching suppliers: " + e.getMessage());
            return null;
        }
    }

    public int countSuppliers() {
        try {
            return supplierDAO.count();
        } catch (SQLException e) {
            System.err.println("Error counting suppliers: " + e.getMessage());
            return 0;
        }
    }
}
