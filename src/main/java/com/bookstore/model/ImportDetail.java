package com.bookstore.model;

/**
 * Model for ImportDetail - Chi tiết đơn nhập
 */
public class ImportDetail {
    private int maChiTiet;
    private int maDonNhap;
    private int maSach;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    // Related object for display
    private Book sach;

    // Constructors
    public ImportDetail() {
    }

    public ImportDetail(int maChiTiet, int maDonNhap, int maSach,
                        int soLuong, double donGia, double thanhTien) {
        this.maChiTiet = maChiTiet;
        this.maDonNhap = maDonNhap;
        this.maSach = maSach;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    // Getters and Setters
    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(int maDonNhap) {
        this.maDonNhap = maDonNhap;
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
        this.thanhTien = this.soLuong * this.donGia;
    }
}
