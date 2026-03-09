package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.util.FormatUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

/**
 * Inventory Controller
 */
public class InventoryController {

    @FXML
    private TableView<Book> tableInventory;
    @FXML
    private TableColumn<Book, Integer> colMaSach;
    @FXML
    private TableColumn<Book, String> colTenSach;
    @FXML
    private TableColumn<Book, String> colTheLoai;
    @FXML
    private TableColumn<Book, Integer> colSoLuong;
    @FXML
    private TableColumn<Book, Double> colGiaBia;
    @FXML
    private TableColumn<Book, Double> colGiaTri;

    private BookService bookService;
    private ObservableList<Book> bookList;

    public InventoryController() {
        this.bookService = new BookService();
        this.bookList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadInventory();
    }

    private void setupTableColumns() {
        colMaSach.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colTheLoai.setCellValueFactory(new PropertyValueFactory<>("theLoai"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
        colGiaBia.setCellValueFactory(new PropertyValueFactory<>("giaBia"));

        colGiaBia.setCellFactory(column -> new TableCell<Book, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("");
                else setText(FormatUtil.formatCurrency(item));
            }
        });

        colGiaTri.setCellFactory(column -> new TableCell<Book, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setText("");
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    double value = book.getSoLuongTon() * book.getGiaBia();
                    setText(FormatUtil.formatCurrency(value));
                }
            }
        });
    }

    private void loadInventory() {
        List<Book> books = bookService.getAllBooks();
        if (books != null) {
            bookList.clear();
            bookList.addAll(books);
            tableInventory.setItems(bookList);
        }
    }
}
