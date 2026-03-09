package com.bookstore.util;

import com.bookstore.model.Book;
import com.bookstore.model.Invoice;
import com.bookstore.service.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Function;

/**
 * Search Utility - Tìm kiếm nâng cao
 */
public class SearchUtil {

    /**
     * Tìm kiếm với nhiều tiêu chí
     */
    public static <T> List<T> search(List<T> list, List<Predicate<T>> predicates) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            boolean match = true;
            for (Predicate<T> predicate : predicates) {
                if (!predicate.test(item)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Tìm kiếm theo nhiều trường
     */
    public static <T> List<T> searchByFields(List<T> list, String keyword, List<Function<T, String>> fieldGetters) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return list;
        }

        String lowerKeyword = keyword.toLowerCase().trim();
        List<T> result = new ArrayList<>();

        for (T item : list) {
            for (Function<T, String> getter : fieldGetters) {
                String value = getter.apply(item);
                if (value != null && value.toLowerCase().contains(lowerKeyword)) {
                    result.add(item);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Tìm kiếm sách đơn giản theo keyword và category
     */
    public static List<Book> searchBooks(String keyword, String category) {
        BookService bookService = new BookService();
        List<Book> books = bookService.getAllBooks();
        if (books == null) return new ArrayList<>();

        return books.stream()
                .filter(book -> {
                    // Keyword search
                    if (keyword != null && !keyword.isEmpty()) {
                        boolean match = false;
                        String lower = keyword.toLowerCase();
                        if (book.getTenSach() != null && book.getTenSach().toLowerCase().contains(lower)) match = true;
                        if (book.getTacGia() != null && book.getTacGia().toLowerCase().contains(lower)) match = true;
                        if (book.getNhaXuatBan() != null && book.getNhaXuatBan().toLowerCase().contains(lower)) match = true;
                        if (!match) return false;
                    }

                    // Category filter
                    if (category != null && !category.isEmpty() && !category.equals("Tất cả")) {
                        if (book.getTheLoai() == null || !book.getTheLoai().equals(category)) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Tìm kiếm sách nâng cao
     */
    public static List<Book> searchBooks(
            List<com.bookstore.model.Book> books,
            String keyword,
            String category,
            String author,
            Integer minYear,
            Integer maxYear,
            Double minPrice,
            Double maxPrice) {

        return books.stream()
                .filter(book -> {
                    // Keyword search
                    if (keyword != null && !keyword.isEmpty()) {
                        boolean match = false;
                        String lower = keyword.toLowerCase();
                        if (book.getTenSach() != null && book.getTenSach().toLowerCase().contains(lower)) match = true;
                        if (book.getTacGia() != null && book.getTacGia().toLowerCase().contains(lower)) match = true;
                        if (book.getNhaXuatBan() != null && book.getNhaXuatBan().toLowerCase().contains(lower)) match = true;
                        if (!match) return false;
                    }

                    // Category filter
                    if (category != null && !category.isEmpty() && !category.equals("Tất cả")) {
                        if (book.getTheLoai() == null || !book.getTheLoai().equals(category)) {
                            return false;
                        }
                    }

                    // Author filter
                    if (author != null && !author.isEmpty()) {
                        if (book.getTacGia() == null || !book.getTacGia().toLowerCase().contains(author.toLowerCase())) {
                            return false;
                        }
                    }

                    // Year range
                    if (minYear != null && book.getNamXuatBan() < minYear) {
                        return false;
                    }
                    if (maxYear != null && book.getNamXuatBan() > maxYear) {
                        return false;
                    }

                    // Price range
                    if (minPrice != null && book.getGiaBia() < minPrice) {
                        return false;
                    }
                    if (maxPrice != null && book.getGiaBia() > maxPrice) {
                        return false;
                    }

                    return true;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Tìm kiếm hóa đơn đơn giản theo keyword
     */
    public static List<Invoice> searchInvoices(List<Invoice> invoices, String keyword) {
        if (invoices == null) return new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return invoices;

        String lower = keyword.toLowerCase();
        return invoices.stream()
                .filter(inv -> {
                    if (inv.getMaHoaDonString() != null && inv.getMaHoaDonString().toLowerCase().contains(lower)) return true;
                    if (inv.getGhiChu() != null && inv.getGhiChu().toLowerCase().contains(lower)) return true;
                    return false;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Tìm kiếm hóa đơn nâng cao
     */
    public static List<Invoice> searchInvoices(
            List<com.bookstore.model.Invoice> invoices,
            String keyword,
            java.util.Date fromDate,
            java.util.Date toDate,
            String status,
            Double minTotal,
            Double maxTotal) {

        return invoices.stream()
                .filter(inv -> {
                    // Keyword
                    if (keyword != null && !keyword.isEmpty()) {
                        boolean match = false;
                        String lower = keyword.toLowerCase();
                        if (inv.getMaHoaDonString() != null && inv.getMaHoaDonString().toLowerCase().contains(lower)) match = true;
                        if (inv.getGhiChu() != null && inv.getGhiChu().toLowerCase().contains(lower)) match = true;
                        if (!match) return false;
                    }

                    // Date range
                    if (fromDate != null && inv.getNgayLap().before(fromDate)) {
                        return false;
                    }
                    if (toDate != null && inv.getNgayLap().after(toDate)) {
                        return false;
                    }

                    // Status
                    if (status != null && !status.isEmpty() && !status.equals("Tất cả")) {
                        if (inv.getTrangThai() == null || !inv.getTrangThai().equals(status)) {
                            return false;
                        }
                    }

                    // Total range
                    if (minTotal != null && inv.getThanhToan() < minTotal) {
                        return false;
                    }
                    if (maxTotal != null && inv.getThanhToan() > maxTotal) {
                        return false;
                    }

                    return true;
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
