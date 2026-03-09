package com.bookstore.controller;

import com.bookstore.model.Invoice;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.InvoiceService;
import com.bookstore.util.ChartUtil;
import com.bookstore.util.ExportPDFUtil;
import com.bookstore.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Statistics Controller
 */
public class StatisticsController {

    @FXML
    private Label lblTotalRevenue;
    @FXML
    private Label lblTotalInvoices;
    @FXML
    private Label lblAvgInvoice;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private ComboBox<String> cboPeriod;
    @FXML
    private AnchorPane chartRevenuePane;
    @FXML
    private AnchorPane chartCategoryPane;

    private InvoiceService invoiceService;
    private BookService bookService;
    private CustomerService customerService;

    public StatisticsController() {
        this.invoiceService = new InvoiceService();
        this.bookService = new BookService();
        this.customerService = new CustomerService();
    }

    @FXML
    public void initialize() {
        // Set default date range (last 7 days)
        dpEndDate.setValue(LocalDate.now());
        dpStartDate.setValue(LocalDate.now().minusDays(7));

        // Initialize period ComboBox
        if (cboPeriod != null) {
            cboPeriod.getItems().addAll("7 ngày gần nhất", "30 ngày gần nhất", "90 ngày gần nhất", "Tùy chọn");
            cboPeriod.setValue("7 ngày gần nhất");
        }
    }

    @FXML
    private void handleGenerate() {
        LocalDate startLocal = dpStartDate.getValue();
        LocalDate endLocal = dpEndDate.getValue();

        if (startLocal == null || endLocal == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn ngày bắt đầu và kết thúc!");
            return;
        }

        Date startDate = Date.from(startLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Get statistics
        double revenue = invoiceService.getRevenueByDateRange(startDate, endDate);
        List<Invoice> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate);
        int count = invoices != null ? invoices.size() : 0;
        double avg = count > 0 ? revenue / count : 0;

        // Update labels
        lblTotalRevenue.setText(FormatUtil.formatCurrency(revenue));
        lblTotalInvoices.setText(String.valueOf(count));
        lblAvgInvoice.setText(FormatUtil.formatCurrency(avg));

        // Generate charts
        generateRevenueChart(invoices);
        generateCategoryChart();
    }

    private void generateRevenueChart(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            chartRevenuePane.getChildren().clear();
            return;
        }

        // Group by date
        Map<String, Double> revenueByDate = invoices.stream()
                .collect(Collectors.groupingBy(
                        inv -> FormatUtil.formatDate(inv.getNgayLap()),
                        Collectors.summingDouble(Invoice::getThanhToan)
                ));

        List<String> dates = new ArrayList<>(revenueByDate.keySet());
        List<Double> revenues = new ArrayList<>(revenueByDate.values());

        Chart chart = ChartUtil.createRevenueBarChart(dates, revenues);

        chartRevenuePane.getChildren().clear();
        AnchorPane.setTopAnchor(chart, 0.0);
        AnchorPane.setBottomAnchor(chart, 0.0);
        AnchorPane.setLeftAnchor(chart, 0.0);
        AnchorPane.setRightAnchor(chart, 0.0);
        chartRevenuePane.getChildren().add(chart);
    }

    private void generateCategoryChart() {
        // Get books grouped by category
        List<com.bookstore.model.Book> books = bookService.getAllBooks();
        if (books == null || books.isEmpty()) {
            chartCategoryPane.getChildren().clear();
            return;
        }

        Map<String, Integer> categoryCount = books.stream()
                .collect(Collectors.groupingBy(
                        book -> book.getTheLoai() != null ? book.getTheLoai() : "Chưa phân loại",
                        Collectors.summingInt(book -> 1)
                ));

        Chart chart = ChartUtil.createCategoryPieChart(categoryCount);

        chartCategoryPane.getChildren().clear();
        AnchorPane.setTopAnchor(chart, 0.0);
        AnchorPane.setBottomAnchor(chart, 0.0);
        AnchorPane.setLeftAnchor(chart, 0.0);
        AnchorPane.setRightAnchor(chart, 0.0);
        chartCategoryPane.getChildren().add(chart);
    }

    @FXML
    private void handleExport() {
        LocalDate startLocal = dpStartDate.getValue();
        LocalDate endLocal = dpEndDate.getValue();

        if (startLocal == null || endLocal == null) {
            startLocal = LocalDate.now().minusDays(7);
            endLocal = LocalDate.now();
        }

        Date startDate = Date.from(startLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Invoice> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate);
        if (invoices == null || invoices.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Không có dữ liệu để xuất!");
            return;
        }

        // Create report data
        List<Object[]> data = new ArrayList<>();
        double totalRevenue = 0;
        for (Invoice inv : invoices) {
            Object[] row = new Object[3];
            row[0] = inv.getMaHoaDonString();
            row[1] = FormatUtil.formatDate(inv.getNgayLap());
            row[2] = inv.getThanhToan();
            data.add(row);
            totalRevenue += inv.getThanhToan();
        }

        // Show save dialog
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Lưu file PDF");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("BaoCaoDoanhThu_" + System.currentTimeMillis() + ".pdf");

        java.io.File file = fileChooser.showSaveDialog(chartRevenuePane.getScene().getWindow());
        if (file != null) {
            try {
                boolean success = ExportPDFUtil.exportRevenueReport(data, totalRevenue, file.getAbsolutePath());
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất PDF thành công!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xuất PDF thất bại!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xuất PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
