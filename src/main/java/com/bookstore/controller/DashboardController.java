package com.bookstore.controller;

import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.InvoiceService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Date;

/**
 * Dashboard Controller - Hiển thị tổng quan
 */
public class DashboardController {

    @FXML
    private Label lblTotalBooks;
    @FXML
    private Label lblTotalCustomers;
    @FXML
    private Label lblTotalInvoicesToday;
    @FXML
    private Label lblRevenueToday;

    private BookService bookService;
    private CustomerService customerService;
    private InvoiceService invoiceService;

    public DashboardController() {
        this.bookService = new BookService();
        this.customerService = new CustomerService();
        this.invoiceService = new InvoiceService();
    }

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        // Load statistics
        lblTotalBooks.setText(String.valueOf(bookService.countBooks()));
        lblTotalCustomers.setText(String.valueOf(customerService.countCustomers()));

        // Today's data
        double revenue = invoiceService.getRevenueByDate(new Date());
        lblRevenueToday.setText(formatCurrency(revenue));
        lblTotalInvoicesToday.setText(String.valueOf(
            invoiceService.getInvoicesByDateRange(new Date(), new Date()).size()
        ));
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f ₫", amount);
    }
}
