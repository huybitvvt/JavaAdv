package com.bookstore.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.bookstore.model.Invoice;
import com.bookstore.model.InvoiceDetail;
import com.bookstore.model.Customer;
import com.bookstore.model.Employee;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Export PDF Utility - Xuất file PDF
 */
public class ExportPDFUtil {

    /**
     * Xuất hóa đơn ra PDF
     */
    public static boolean exportInvoice(Invoice invoice, List<InvoiceDetail> details, String filePath) {
        try {
            // Create PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Header
            Paragraph header = new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(header);

            // Invoice info
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            document.add(new Paragraph("Mã hóa đơn: " + invoice.getMaHoaDonString()));
            document.add(new Paragraph("Ngày lập: " + sdf.format(invoice.getNgayLap())));
            document.add(new Paragraph("Nhân viên: " + (invoice.getNhanVien() != null ? invoice.getNhanVien().getHoTen() : "N/A")));
            document.add(new Paragraph("Khách hàng: " + (invoice.getKhachHang() != null ? invoice.getKhachHang().getHoTen() : "Khách vãng lai")));
            document.add(new Paragraph("\n"));

            // Create table for details
            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2}))
                    .useAllAvailableWidth();

            // Table header
            table.addHeaderCell("Tên sách");
            table.addHeaderCell("Số lượng");
            table.addHeaderCell("Đơn giá");
            table.addHeaderCell("Giảm giá");
            table.addHeaderCell("Thành tiền");

            // Table rows
            for (InvoiceDetail detail : details) {
                table.addCell(detail.getSach() != null ? detail.getSach().getTenSach() : "N/A");
                table.addCell(String.valueOf(detail.getSoLuong()));
                table.addCell(formatCurrency(detail.getDonGia()));
                table.addCell(String.format("%.0f%%", detail.getGiamGia()));
                table.addCell(formatCurrency(detail.getThanhTien()));
            }

            document.add(table);

            // Total
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Tổng tiền: " + formatCurrency(invoice.getTongTien()))
                    .setBold());
            document.add(new Paragraph("Giảm giá: " + formatCurrency(invoice.getGiamGia()))
                    .setBold());
            document.add(new Paragraph("Thanh toán: " + formatCurrency(invoice.getThanhToan()))
                    .setBold()
                    .setFontSize(14));

            // Footer
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Cảm ơn quý khách!")
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xuất báo cáo doanh thu
     */
    public static boolean exportRevenueReport(List<Object[]> data, double totalRevenue, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Header
            document.add(new Paragraph("BÁO CÁO DOANH THU")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Ngày lập: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
            document.add(new Paragraph("\n"));

            // Table
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3, 2}))
                    .useAllAvailableWidth();

            table.addHeaderCell("Mã hóa đơn");
            table.addHeaderCell("Ngày");
            table.addHeaderCell("Doanh thu");

            for (Object[] row : data) {
                table.addCell(String.valueOf(row[0]));
                table.addCell(String.valueOf(row[1]));
                table.addCell(formatCurrency(Double.parseDouble(String.valueOf(row[2]))));
            }

            document.add(table);

            // Total
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Tổng doanh thu: " + formatCurrency(totalRevenue))
                    .setBold()
                    .setFontSize(14));

            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String formatCurrency(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }
}
