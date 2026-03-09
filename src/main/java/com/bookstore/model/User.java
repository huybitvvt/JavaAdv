package com.bookstore.model;

import java.util.Date;

/**
 * Model for User - Tài khoản người dùng (đăng nhập)
 */
public class User {
    private int maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro; // Admin, NhanVien
    private int maNhanVien;
    private Date ngayTao;
    private Date ngayCapNhat;
    private String trangThai; // HoatDong, Khoa

    // Related object
    private Employee nhanVien;

    // Constructors
    public User() {
    }

    public User(int maTaiKhoan, String tenDangNhap, String matKhau, String vaiTro, int maNhanVien) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.maNhanVien = maNhanVien;
    }

    // Getters and Setters
    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
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

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Employee getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(Employee nhanVien) {
        this.nhanVien = nhanVien;
    }

    /**
     * Kiểm tra có phải Admin không
     */
    public boolean isAdmin() {
        return "Admin".equals(vaiTro);
    }

    /**
     * Kiểm tra tài khoản có hoạt động không
     */
    public boolean isActive() {
        return "HoatDong".equals(trangThai);
    }
}
