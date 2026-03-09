package com.bookstore.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

/**
 * Export Excel Utility - Xuất file Excel
 */
public class ExportExcelUtil {

    /**
     * Xuất danh sách sách ra Excel
     */
    public static boolean exportBooks(List<com.bookstore.model.Book> books, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách sách");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Mã", "Tên sách", "Tác giả", "NXB", "Thể loại", "Năm XB", "Gía bìa", "Số lượng tồn"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // Data
            int rowNum = 1;
            for (com.bookstore.model.Book book : books) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getMaSach());
                row.createCell(1).setCellValue(book.getTenSach());
                row.createCell(2).setCellValue(book.getTacGia());
                row.createCell(3).setCellValue(book.getNhaXuatBan());
                row.createCell(4).setCellValue(book.getTheLoai());
                row.createCell(5).setCellValue(book.getNamXuatBan());
                row.createCell(6).setCellValue(book.getGiaBia());
                row.createCell(7).setCellValue(book.getSoLuongTon());
            }

            // Auto size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xuất danh sách khách hàng ra Excel
     */
    public static boolean exportCustomers(List<com.bookstore.model.Customer> customers, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách khách hàng");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Mã", "Họ tên", "Giới tính", "Ngày sinh", "SĐT", "Email", "Địa chỉ", "Điểm tích lũy"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // Data
            int rowNum = 1;
            for (com.bookstore.model.Customer customer : customers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(customer.getMaKhachHang());
                row.createCell(1).setCellValue(customer.getHoTen());
                row.createCell(2).setCellValue(customer.getGioiTinh());
                row.createCell(3).setCellValue(FormatUtil.formatDate(customer.getNgaySinh()));
                row.createCell(4).setCellValue(customer.getSoDienThoai());
                row.createCell(5).setCellValue(customer.getEmail());
                row.createCell(6).setCellValue(customer.getDiaChi());
                row.createCell(7).setCellValue(customer.getDiemTichLuy());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xuất danh sách hóa đơn ra Excel
     */
    public static boolean exportInvoices(List<com.bookstore.model.Invoice> invoices, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hóa đơn");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Mã HĐ", "Ngày lập", "Khách hàng", "Nhân viên", "Tổng tiền", "Giảm giá", "Thanh toán", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // Data
            int rowNum = 1;
            for (com.bookstore.model.Invoice inv : invoices) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(inv.getMaHoaDonString());
                row.createCell(1).setCellValue(FormatUtil.formatDateTime(inv.getNgayLap()));
                row.createCell(2).setCellValue(inv.getKhachHang() != null ? inv.getKhachHang().getHoTen() : "Khách vãng lai");
                row.createCell(3).setCellValue(inv.getNhanVien() != null ? inv.getNhanVien().getHoTen() : "N/A");
                row.createCell(4).setCellValue(inv.getTongTien());
                row.createCell(5).setCellValue(inv.getGiamGia());
                row.createCell(6).setCellValue(inv.getThanhToan());
                row.createCell(7).setCellValue(inv.getTrangThai());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
