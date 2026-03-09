package com.bookstore.model;

import java.util.Date;
import java.util.List;

/**
 * Model for ImportOrder - Đơn nhập hàng
 */
public class ImportOrder {
    private int maDonNhap;
    private String maDonNhapString; // PN001, PN002...
    private int maNhaCungCap;
    private int maNhanVien;
    private Date ngayNhap;
    private double tongTien;
    private String ghiChu;
    private String trangThai; // DaNhap, DangCho, DaHuy
    private Date ngayTao;
    private Date ngayCapNhat;

    // Related objects
    private Supplier nhaCungCap;
    private Employee nhanVien;
    private List<ImportDetail> chiTietDonNhap;

    // Constructors
    public ImportOrder() {
    }

    public ImportOrder(int maDonNhap, int maNhaCungCap, int maNhanVien,
                        Date ngayNhap, double tongTien, String trangThai) {
        this.maDonNhap = maDonNhap;
        this.maNhaCungCap = maNhaCungCap;
        this.maNhanVien = maNhanVien;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaDonNhap() {
        return maDonNhap;
    }

    public void setMaDonNhap(int maDonNhap) {
        this.maDonNhap = maDonNhap;
    }

    public String getMaDonNhapString() {
        return maDonNhapString;
    }

    public void setMaDonNhapString(String maDonNhapString) {
        this.maDonNhapString = maDonNhapString;
    }

    public int getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(int maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
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
    public Supplier getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(Supplier nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public Employee getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(Employee nhanVien) {
        this.nhanVien = nhanVien;
    }

    public List<ImportDetail> getChiTietDonNhap() {
        return chiTietDonNhap;
    }

    public void setChiTietDonNhap(List<ImportDetail> chiTietDonNhap) {
        this.chiTietDonNhap = chiTietDonNhap;
    }
}
