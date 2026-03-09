package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Invoice;
import com.bookstore.model.InvoiceDetail;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.InvoiceService;
import com.bookstore.util.FormatUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;

import java.util.ArrayList;
import java.util.List;

/**
 * POS Controller - Xử lý bán hàng
 */
public class POSController {

    @FXML
    private TextField txtSearchBook;
    @FXML
    private ComboBox<Customer> cboCustomer;
    @FXML
    private ComboBox<String> cboPayment;
    @FXML
    private TableView<Book> tableBooks;
    @FXML
    private TableColumn<Book, Integer> colBookMa;
    @FXML
    private TableColumn<Book, String> colBookTen;
    @FXML
    private TableColumn<Book, Double> colBookGia;
    @FXML
    private TableColumn<Book, Integer> colBookTon;
    @FXML
    private TableView<InvoiceDetail> tableCart;
    @FXML
    private Label lblTongTien;
    @FXML
    private Label lblThanhToan;
    @FXML
    private TextField txtGiamGia;

    private BookService bookService;
    private CustomerService customerService;
    private InvoiceService invoiceService;
    private ObservableList<Book> bookList;
    private ObservableList<InvoiceDetail> cartList;
    private List<InvoiceDetail> currentCart;

    public POSController() {
        this.bookService = new BookService();
        this.customerService = new CustomerService();
        this.invoiceService = new InvoiceService();
        this.bookList = FXCollections.observableArrayList();
        this.cartList = FXCollections.observableArrayList();
        this.currentCart = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadBooks();
        loadCustomers();
        setupPaymentMethods();
    }

    private void setupTableColumns() {
        colBookMa.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        colBookTen.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colBookGia.setCellValueFactory(new PropertyValueFactory<>("giaBia"));
        colBookTon.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));

        // Custom cell for price
        colBookGia.setCellFactory(column -> new TableCell<Book, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("");
                else setText(FormatUtil.formatCurrency(item));
            }
        });
    }

    private void loadBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books != null) {
            bookList.clear();
            bookList.addAll(books);
            tableBooks.setItems(bookList);
        }
    }

    private void loadCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers != null) {
            cboCustomer.getItems().clear();
            cboCustomer.getItems().add(null); // For guest
            cboCustomer.getItems().addAll(customers);
        }
    }

    private void setupPaymentMethods() {
        cboPayment.getItems().addAll("Tiền mặt", "Chuyển khoản", "Quẹt thẻ");
    }

    @FXML
    private void handleAddToCart() {
        Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn sách!");
            return;
        }

        if (selectedBook.getSoLuongTon() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Sách đã hết hàng!");
            return;
        }

        // Add to cart
        InvoiceDetail detail = new InvoiceDetail();
        detail.setMaSach(selectedBook.getMaSach());
        detail.setSach(selectedBook);
        detail.setSoLuong(1);
        detail.setDonGia(selectedBook.getGiaBia());
        detail.tinhThanhTien();

        currentCart.add(detail);
        cartList.add(detail);
        tableCart.setItems(cartList);

        updateTotal();
    }

    @FXML
    private void handleCheckout() {
        if (currentCart.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giỏ hàng trống!");
            return;
        }

        // Create invoice
        Invoice invoice = new Invoice();
        invoice.setMaKhachHang(cboCustomer.getValue() != null ? cboCustomer.getValue().getMaKhachHang() : 0);
        invoice.setMaNhanVien(1); // TODO: Get from logged in user
        invoice.setTongTien(calculateTotal());
        invoice.setGiamGia(Double.parseDouble(txtGiamGia.getText()));
        invoice.setThanhToan(calculateTotal() * (1 - invoice.getGiamGia() / 100));
        invoice.setPhuongThucThanhToan(cboPayment.getValue());

        if (invoiceService.createInvoice(invoice, currentCart)) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thanh toán thành công!");
            handleCancel();
            loadBooks();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Thanh toán thất bại!");
        }
    }

    @FXML
    private void handleCancel() {
        currentCart.clear();
        cartList.clear();
        tableCart.setItems(cartList);
        updateTotal();
    }

    private void updateTotal() {
        double total = calculateTotal();
        lblTongTien.setText(FormatUtil.formatCurrency(total));

        double discount = Double.parseDouble(txtGiamGia.getText());
        double payment = total * (1 - discount / 100);
        lblThanhToan.setText(FormatUtil.formatCurrency(payment));
    }

    private double calculateTotal() {
        return currentCart.stream().mapToDouble(InvoiceDetail::getThanhTien).sum();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
