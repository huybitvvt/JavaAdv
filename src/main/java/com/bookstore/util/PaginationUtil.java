package com.bookstore.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination Utility - Phân trang cho TableView
 */
public class PaginationUtil<T> {

    private final TableView<T> tableView;
    private final Pagination pagination;
    private final ComboBox<Integer> cboPageSize;

    private List<T> fullData;
    private int totalItems;
    private int itemsPerPage = 10;
    private int currentPage = 0;

    /**
     * Constructor
     */
    public PaginationUtil(TableView<T> tableView, Pagination pagination, ComboBox<Integer> cboPageSize) {
        this.tableView = tableView;
        this.pagination = pagination;
        this.cboPageSize = cboPageSize;

        // Setup page size combo
        cboPageSize.getItems().addAll(5, 10, 20, 50, 100);
        cboPageSize.setValue(10);

        // Setup pagination change listener
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            currentPage = newVal.intValue();
            displayPage(currentPage);
        });

        cboPageSize.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                itemsPerPage = newVal;
                currentPage = 0;
                pagination.setCurrentPageIndex(0);
                updatePagination();
                displayPage(0);
            }
        });
    }

    /**
     * Set data source
     */
    public void setData(List<T> data) {
        this.fullData = new ArrayList<>(data);
        this.totalItems = fullData.size();
        currentPage = 0;
        pagination.setCurrentPageIndex(0);
        updatePagination();
        displayPage(0);
    }

    /**
     * Update pagination controls
     */
    private void updatePagination() {
        int pageCount = (int) Math.ceil((double) totalItems / itemsPerPage);
        if (pageCount == 0) pageCount = 1;
        pagination.setPageCount(pageCount);
    }

    /**
     * Display specific page
     */
    private void displayPage(int page) {
        if (fullData == null || fullData.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList());
            return;
        }

        int fromIndex = page * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

        if (fromIndex >= totalItems) {
            fromIndex = 0;
            toIndex = Math.min(itemsPerPage, totalItems);
        }

        List<T> pageData = fullData.subList(fromIndex, toIndex);
        tableView.setItems(FXCollections.observableArrayList(pageData));
    }

    /**
     * Refresh current page
     */
    public void refresh() {
        displayPage(currentPage);
    }

    /**
     * Get current page number (1-based)
     */
    public int getCurrentPageNumber() {
        return currentPage + 1;
    }

    /**
     * Get total pages
     */
    public int getTotalPages() {
        return (int) Math.ceil((double) totalItems / itemsPerPage);
    }

    /**
     * Get total items
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * Go to first page
     */
    public void firstPage() {
        currentPage = 0;
        pagination.setCurrentPageIndex(0);
        displayPage(0);
    }

    /**
     * Go to last page
     */
    public void lastPage() {
        int lastPage = getTotalPages() - 1;
        if (lastPage < 0) lastPage = 0;
        currentPage = lastPage;
        pagination.setCurrentPageIndex(lastPage);
        displayPage(lastPage);
    }
}
