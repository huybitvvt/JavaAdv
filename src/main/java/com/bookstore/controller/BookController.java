package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.util.FormatUtil;
import com.bookstore.util.ExportExcelUtil;
import com.bookstore.util.SearchUtil;
import com.bookstore.util.PaginationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Book Controller - Xử lý quản lý sách
 */
public class BookController {

    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<String> cboCategory;
    @FXML
    private TableView<Book> tableBooks;
    @FXML
    private TableColumn<Book, Integer> colMaSach;
    @FXML
    private TableColumn<Book, String> colTenSach;
    @FXML
    private TableColumn<Book, String> colTacGia;
    @FXML
    private TableColumn<Book, String> colNXB;
    @FXML
    private TableColumn<Book, String> colTheLoai;
    @FXML
    private TableColumn<Book, Integer> colNamXB;
    @FXML
    private TableColumn<Book, Double> colGiaBia;
    @FXML
    private TableColumn<Book, Integer> colSoLuong;
    @FXML
    private TableColumn<Book, String> colHanhDong;
    @FXML
    private Label lblTotal;
    @FXML
    private ComboBox<Integer> cboPageSize;
    @FXML
    private Pagination pagination;

    private BookService bookService;
    private ObservableList<Book> bookList;
    private PaginationUtil paginationUtil;
    private List<Book> allBooks;

    public BookController() {
        this.bookService = new BookService();
        this.bookList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        initPagination();
        loadBooks();
        loadCategories();
    }

    private void initPagination() {
        paginationUtil = new PaginationUtil(tableBooks, pagination, cboPageSize);
    }

    private void setupTableColumns() {
        colMaSach.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colTacGia.setCellValueFactory(new PropertyValueFactory<>("tacGia"));
        colNXB.setCellValueFactory(new PropertyValueFactory<>("nhaXuatBan"));
        colTheLoai.setCellValueFactory(new PropertyValueFactory<>("theLoai"));
        colNamXB.setCellValueFactory(new PropertyValueFactory<>("namXuatBan"));
        colGiaBia.setCellValueFactory(new PropertyValueFactory<>("giaBia"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));

        // Custom cell for price
        colGiaBia.setCellFactory(column -> new TableCell<Book, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(FormatUtil.formatCurrency(item));
                }
            }
        });

        // Action column
        colHanhDong.setCellFactory(column -> new TableCell<Book, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    HBox actions = new HBox(5);
                    Button btnEdit = new Button("Sửa");
                    btnEdit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    btnEdit.setOnAction(e -> handleEdit(book));

                    Button btnDelete = new Button("Xóa");
                    btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    btnDelete.setOnAction(e -> handleDelete(book));

                    actions.getChildren().addAll(btnEdit, btnDelete);
                    setGraphic(actions);
                }
            }
        });
    }

    private void loadBooks() {
        allBooks = bookService.getAllBooks();
        if (allBooks != null) {
            if (paginationUtil != null) {
                paginationUtil.setData(allBooks);
            }
            lblTotal.setText("Tổng số: " + allBooks.size());
        }
    }

    private void loadCategories() {
        List<String> categories = bookService.getAllCategories();
        if (categories != null) {
            cboCategory.getItems().clear();
            cboCategory.getItems().add("Tất cả");
            cboCategory.getItems().addAll(categories);
            cboCategory.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        String category = cboCategory.getValue();

        List<Book> books;
        if (!keyword.isEmpty()) {
            books = bookService.searchBooks(keyword);
        } else if (category != null && !category.equals("Tất cả")) {
            books = bookService.getBooksByCategory(category);
        } else {
            books = bookService.getAllBooks();
        }

        if (books != null) {
            allBooks = books;
            paginationUtil.setData(books);
            lblTotal.setText("Tổng số: " + books.size());
        }
    }

    @FXML
    private void handleAdd() {
        openBookDialog(null);
    }

    @FXML
    private void handleEdit(Book book) {
        openBookDialog(book);
    }

    @FXML
    private void handleDelete(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa sách");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa sách: " + book.getTenSach() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                if (bookService.deleteBook(book.getMaSach())) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa sách thành công!");
                    loadBooks();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa sách!");
                }
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        cboCategory.getSelectionModel().selectFirst();
        loadBooks();
        pagination.setCurrentPageIndex(0);
    }

    @FXML
    private void handleExport() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xuất dữ liệu");
        alert.setHeaderText("Chọn loại xuất file:");

        ButtonType btnExcel = new ButtonType("Xuất Excel");
        ButtonType btnCancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnExcel, btnCancel);

        alert.showAndWait().ifPresent(result -> {
            if (result == btnExcel) {
                exportToExcel();
            }
        });
    }

    private void exportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("DanhSachSach_" + System.currentTimeMillis() + ".xlsx");

        File file = fileChooser.showSaveDialog(tableBooks.getScene().getWindow());
        if (file != null) {
            try {
                List<Book> books = bookService.getAllBooks();
                if (books != null && !books.isEmpty()) {
                    boolean success = ExportExcelUtil.exportBooks(books, file.getAbsolutePath());
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

    @FXML
    private void handleAdvancedSearch() {
        // Advanced search with multiple criteria
        String keyword = txtSearch.getText().trim();
        String category = cboCategory.getValue();

        List<Book> results;
        if (!keyword.isEmpty()) {
            results = SearchUtil.searchBooks(keyword, category);
        } else if (category != null && !category.equals("Tất cả")) {
            results = bookService.getBooksByCategory(category);
        } else {
            results = bookService.getAllBooks();
        }

        if (results != null) {
            allBooks = results;
            paginationUtil.setData(results);
            lblTotal.setText("Tổng số: " + results.size());
        }
    }

    private void openBookDialog(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bookstore/view/book_dialog.fxml"));
            Parent root = loader.load();

            BookDialogController controller = loader.getController();
            controller.setBook(book);

            Stage stage = new Stage();
            stage.setTitle(book == null ? "Thêm sách mới" : "Sửa thông tin sách");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadBooks();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở dialog: " + e.getMessage());
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
