package com.bookstore.service;

import com.bookstore.dao.EmployeeDAO;
import com.bookstore.model.Employee;

import java.sql.SQLException;
import java.util.List;

/**
 * Employee Service - Business logic layer for Employee
 */
public class EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeDAO.getAll();
        } catch (SQLException e) {
            System.err.println("Error getting all employees: " + e.getMessage());
            return null;
        }
    }

    public Employee getEmployeeById(int id) {
        try {
            return employeeDAO.getById(id);
        } catch (SQLException e) {
            System.err.println("Error getting employee by id: " + e.getMessage());
            return null;
        }
    }

    public boolean addEmployee(Employee employee) {
        try {
            if (employee.getHoTen() == null || employee.getHoTen().trim().isEmpty()) {
                System.err.println("Employee name is required");
                return false;
            }
            return employeeDAO.insert(employee);
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(Employee employee) {
        try {
            if (employee.getHoTen() == null || employee.getHoTen().trim().isEmpty()) {
                return false;
            }
            return employeeDAO.update(employee);
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int id) {
        try {
            return employeeDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    public List<Employee> searchEmployees(String keyword) {
        try {
            return employeeDAO.search(keyword);
        } catch (SQLException e) {
            System.err.println("Error searching employees: " + e.getMessage());
            return null;
        }
    }

    public List<Employee> getEmployeesByPosition(String position) {
        try {
            return employeeDAO.getByPosition(position);
        } catch (SQLException e) {
            System.err.println("Error getting employees by position: " + e.getMessage());
            return null;
        }
    }

    public int countEmployees() {
        try {
            return employeeDAO.count();
        } catch (SQLException e) {
            System.err.println("Error counting employees: " + e.getMessage());
            return 0;
        }
    }
}
