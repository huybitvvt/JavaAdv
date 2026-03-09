package com.bookstore.model;

import java.util.Date;
import java.util.List;

/**
 * Model for Invoice - Hóa đơn
 */
public class Invoice {
    private int maHoaDon;
    private String maHoaDonString; // HD001, HD002...
    private int maKhachHang;
    private int maNhanVien;
    private Date ngayLap;
    private double tongTien;
    private double giamGia;
    private double thanhToan;
    private String phuongThucThanhToan; // Tiền mặt, Chuyển khoản
    private String ghiChu;
    private String trangThai; // Đã thanh toán, Chưa thanh toán, Đã hủy
    private Date ngayTao;
    private Date ngayCapNhat;

    // Related objects for display
    private Customer khachHang;
    private Employee nhanVien;
    private List<InvoiceDetail> chiTietHoaDon;

    // Constructors
    public Invoice() {
    }

    public Invoice(int maHoaDon, int maKhachHang, int maNhanVien, Date ngayLap,
                   double tongTien, double giamGia, double thanhToan, String phuongThucThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maKhachHang = maKhachHang;
        this.maNhanVien = maNhanVien;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.giamGia = giamGia;
        this.thanhToan = thanhToan;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    // Getters and Setters
    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaHoaDonString() {
        return maHoaDonString;
    }

    public void setMaHoaDonString(String maHoaDonString) {
        this.maHoaDonString = maHoaDonString;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public double getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(double thanhToan) {
        this.thanhToan = thanhToan;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(Date ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    // Related objects
    public Customer getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(Customer khachHang) {
        this.khachHang = khachHang;
    }

    public Employee getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(Employee nhanVien) {
        this.nhanVien = nhanVien;
    }

    public List<InvoiceDetail> getChiTietHoaDon() {
        return chiTietHoaDon;
    }

    public void setChiTietHoaDon(List<InvoiceDetail> chiTietHoaDon) {
        this.chiTietHoaDon = chiTietHoaDon;
    }
}
