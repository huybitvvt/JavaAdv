package com.bookstore.model;

/**
 * Model for InvoiceDetail - Chi tiết hóa đơn
 */
public class InvoiceDetail {
    private int maChiTiet;
    private int maHoaDon;
    private int maSach;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private double giamGia;

    // Related object for display
    private Book sach;

    // Constructors
    public InvoiceDetail() {
    }

    public InvoiceDetail(int maChiTiet, int maHoaDon, int maSach, int soLuong,
                         double donGia, double thanhTien, double giamGia) {
        this.maChiTiet = maChiTiet;
        this.maHoaDon = maHoaDon;
        this.maSach = maSach;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.giamGia = giamGia;
    }

    // Getters and Setters
    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public Book getSach() {
        return sach;
    }

    public void setSach(Book sach) {
        this.sach = sach;
    }

    /**
     * Tính thành tiền tự động
     */
    public void tinhThanhTien() {
        this.thanhTien = (this.soLuong * this.donGia) * (1 - this.giamGia / 100);
    }
}
