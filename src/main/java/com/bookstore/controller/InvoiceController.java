package com.bookstore.controller;

import com.bookstore.model.Invoice;
import com.bookstore.model.InvoiceDetail;
import com.bookstore.service.InvoiceService;
import com.bookstore.util.FormatUtil;
import com.bookstore.util.ExportPDFUtil;
import com.bookstore.util.ExportExcelUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;

/**
 * Invoice Controller
 */
public class InvoiceController {

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Invoice> tableInvoices;
    @FXML
    private TableColumn<Invoice, String> colMaHD;
    @FXML
    private TableColumn<Invoice, String> colNgayLap;
    @FXML
    private TableColumn<Invoice, String> colKhachHang;
    @FXML
    private TableColumn<Invoice, String> colNhanVien;
    @FXML
    private TableColumn<Invoice, Double> colTongTien;
    @FXML
    private TableColumn<Invoice, Double> colGiamGia;
    @FXML
    private TableColumn<Invoice, Double> colThanhToan;
    @FXML
    private TableColumn<Invoice, String> colTrangThai;
    @FXML
    private TableColumn<Invoice, String> colHanhDong;

    private InvoiceService invoiceService;
    private ObservableList<Invoice> invoiceList;

    public InvoiceController() {
        this.invoiceService = new InvoiceService();
        this.invoiceList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadInvoices();
    }

    private void setupTableColumns() {
        colMaHD.setCellValueFactory(new PropertyValueFactory<>("maHoaDonString"));
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colGiamGia.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
        colThanhToan.setCellValueFactory(new PropertyValueFactory<>("thanhToan"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        colNgayLap.setCellFactory(column -> new TableCell<Invoice, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setText("");
                } else {
                    Invoice inv = getTableView().getItems().get(getIndex());
                    setText(FormatUtil.formatDateTime(inv.getNgayLap()));
                }
            }
        });

        colTongTien.setCellFactory(column -> new TableCell<Invoice, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("");
                else setText(FormatUtil.formatCurrency(item));
            }
        });

        colThanhToan.setCellFactory(column -> new TableCell<Invoice, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("");
                else setText(FormatUtil.formatCurrency(item));
            }
        });

        colHanhDong.setCellFactory(column -> new TableCell<Invoice, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button btnView = new Button("Xem");
                    btnView.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    btnView.setOnAction(e -> viewInvoice(getTableView().getItems().get(getIndex())));
                    setGraphic(btnView);
                }
            }
        });
    }

    private void loadInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        if (invoices != null) {
            invoiceList.clear();
            invoiceList.addAll(invoices);
            tableInvoices.setItems(invoiceList);
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (!keyword.isEmpty()) {
            List<Invoice> invoices = invoiceService.searchInvoices(keyword);
            invoiceList.clear();
            if (invoices != null) invoiceList.addAll(invoices);
        } else {
            loadInvoices();
        }
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        loadInvoices();
    }

    @FXML
    private void handleExport() {
        // Show export options
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xuất dữ liệu");
        alert.setHeaderText("Chọn loại xuất file:");
        alert.setContentText("Chọn loại xuất file?");

        ButtonType btnPDF = new ButtonType("Xuất PDF");
        ButtonType btnExcel = new ButtonType("Xuất Excel");
        ButtonType btnCancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnPDF, btnExcel, btnCancel);

        alert.showAndWait().ifPresent(result -> {
            if (result == btnPDF) {
                exportToPDF();
            } else if (result == btnExcel) {
                exportToExcel();
            }
        });
    }

    private void exportToPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("HoaDon_" + System.currentTimeMillis() + ".pdf");

        File file = fileChooser.showSaveDialog(tableInvoices.getScene().getWindow());
        if (file != null) {
            try {
                // Export all invoices to PDF
                List<Invoice> invoices = invoiceService.getAllInvoices();
                if (invoices != null && !invoices.isEmpty()) {
                    // Create report data
                    java.util.List<Object[]> data = new java.util.ArrayList<>();
                    double totalRevenue = 0;
                    for (Invoice inv : invoices) {
                        Object[] row = new Object[3];
                        row[0] = inv.getMaHoaDonString();
                        row[1] = FormatUtil.formatDate(inv.getNgayLap());
                        row[2] = inv.getThanhToan();
                        data.add(row);
                        totalRevenue += inv.getThanhToan();
                    }

                    boolean success = ExportPDFUtil.exportRevenueReport(data, totalRevenue, file.getAbsolutePath());
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất PDF thành công!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Xuất PDF thất bại!");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Thông báo", "Không có dữ liệu để xuất!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xuất PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void exportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("DanhSachHoaDon_" + System.currentTimeMillis() + ".xlsx");

        File file = fileChooser.showSaveDialog(tableInvoices.getScene().getWindow());
        if (file != null) {
            try {
                List<Invoice> invoices = invoiceService.getAllInvoices();
                if (invoices != null && !invoices.isEmpty()) {
                    boolean success = ExportExcelUtil.exportInvoices(invoices, file.getAbsolutePath());
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất Excel thành công!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Xuất Excel thất bại!");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Thông báo", "Không có dữ liệu để xuất!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xuất Excel: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void viewInvoice(Invoice invoice) {
        showAlert(Alert.AlertType.INFORMATION, "Chi tiết hóa đơn",
                "Mã: " + invoice.getMaHoaDonString() + "\n" +
                "Tổng tiền: " + FormatUtil.formatCurrency(invoice.getTongTien()) + "\n" +
                "Thanh toán: " + FormatUtil.formatCurrency(invoice.getThanhToan()));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
