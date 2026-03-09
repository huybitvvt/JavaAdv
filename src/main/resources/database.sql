-- =====================================================
-- QUẢN LÝ CỬA HÀNG SÁCH - SQL SERVER DATABASE
-- =====================================================

-- Tạo database nếu chưa tồn tại
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'QuanLyCuaHangSach')
BEGIN
    CREATE DATABASE QuanLyCuaHangSach;
END
GO

USE QuanLyCuaHangSach;
GO

-- =====================================================
-- BẢNG: Sách (Books)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sach]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[Sach](
        [MaSach] [int] IDENTITY(1,1) PRIMARY KEY,
        [TenSach] [nvarchar](200) NOT NULL,
        [TacGia] [nvarchar](100),
        [NhaXuatBan] [nvarchar](100),
        [TheLoai] [nvarchar](50),
        [NamXuatBan] [int],
        [GiaBia] [decimal](18, 2) NOT NULL DEFAULT 0,
        [SoLuongTon] [int] NOT NULL DEFAULT 0,
        [MoTa] [nvarchar](max),
        [NgayTao] [datetime] DEFAULT GETDATE(),
        [NgayCapNhat] [datetime] DEFAULT GETDATE()
    );
END
GO

-- =====================================================
-- BẢNG: Khách hàng (Customers)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[KhachHang]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[KhachHang](
        [MaKhachHang] [int] IDENTITY(1,1) PRIMARY KEY,
        [HoTen] [nvarchar](100) NOT NULL,
        [GioiTinh] [nvarchar](10),
        [NgaySinh] [date],
        [SoDienThoai] [nvarchar](20),
        [Email] [nvarchar](100),
        [DiaChi] [nvarchar](200),
        [DiemTichLuy] [int] DEFAULT 0,
        [NgayDangKy] [datetime] DEFAULT GETDATE(),
        [NgayCapNhat] [datetime] DEFAULT GETDATE()
    );
END
GO

-- =====================================================
-- BẢNG: Nhân viên (Employees)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[NhanVien]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[NhanVien](
        [MaNhanVien] [int] IDENTITY(1,1) PRIMARY KEY,
        [HoTen] [nvarchar](100) NOT NULL,
        [GioiTinh] [nvarchar](10),
        [NgaySinh] [date],
        [SoDienThoai] [nvarchar](20),
        [Email] [nvarchar](100),
        [DiaChi] [nvarchar](200),
        [ChucVu] [nvarchar](50) DEFAULT N'Nhân viên',
        [Luong] [decimal](18, 2) DEFAULT 0,
        [NgayVaoLam] [date],
        [TrangThai] [nvarchar](20) DEFAULT N'Hoạt động',
        [NgayTao] [datetime] DEFAULT GETDATE(),
        [NgayCapNhat] [datetime] DEFAULT GETDATE()
    );
END
GO

-- =====================================================
-- BẢNG: Nhà cung cấp (Suppliers)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[NhaCungCap]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[NhaCungCap](
        [MaNhaCungCap] [int] IDENTITY(1,1) PRIMARY KEY,
        [TenNhaCungCap] [nvarchar](200) NOT NULL,
        [DiaChi] [nvarchar](200),
        [SoDienThoai] [nvarchar](20),
        [Email] [nvarchar](100),
        [NguoiLienHe] [nvarchar](100),
        [MoTa] [nvarchar](max),
        [NgayTao] [datetime] DEFAULT GETDATE(),
        [NgayCapNhat] [datetime] DEFAULT GETDATE()
    );
END
GO

-- =====================================================
-- BẢNG: Tài khoản (Users/Accounts)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TaiKhoan]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[TaiKhoan](
        [MaTaiKhoan] [int] IDENTITY(1,1) PRIMARY KEY,
        [TenDangNhap] [nvarchar](50) NOT NULL UNIQUE,
        [MatKhau] [nvarchar](100) NOT NULL,
        [VaiTro] [nvarchar](20) DEFAULT N'Nhân viên', -- Admin, Nhân viên
        [MaNhanVien] [int],
        [TrangThai] [nvarchar](20) DEFAULT N'Hoạt động',
        [NgayTao] [datetime] DEFAULT GETDATE(),
        [NgayCapNhat] [datetime] DEFAULT GETDATE(),
        FOREIGN KEY ([MaNhanVien]) REFERENCES [NhanVien]([MaNhanVien])
    );
END
GO

-- =====================================================
-- BẢNG: Hóa đơn (Invoices)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[HoaDon]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[HoaDon](
        [MaHoaDon] [int] IDENTITY(1,1) PRIMARY KEY,
        [MaHoaDonString] [nvarchar](20) NOT NULL UNIQUE,
        [MaKhachHang] [int],
        [MaNhanVien] [int] NOT NULL,
        [NgayLap] [datetime] DEFAULT GETDATE(),
        [TongTien] [decimal](18, 2) DEFAULT 0,
        [GiamGia] [decimal](18, 2) DEFAULT 0,
        [ThanhToan] [decimal](18, 2) DEFAULT 0,
        [PhuongThucThanhToan] [nvarchar](50) DEFAULT N'Tiền mặt',
        [GhiChu] [nvarchar](500),
        [TrangThai] [nvarchar](30) DEFAULT N'Đã thanh toán',
        [NgayTao] [datetime] DEFAULT GETDATE(),
        [NgayCapNhat] [datetime] DEFAULT GETDATE(),
        FOREIGN KEY ([MaKhachHang]) REFERENCES [KhachHang]([MaKhachHang]),
        FOREIGN KEY ([MaNhanVien]) REFERENCES [NhanVien]([MaNhanVien])
    );
END
GO

-- =====================================================
-- BẢNG: Chi tiết hóa đơn (Invoice Details)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ChiTietHoaDon]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[ChiTietHoaDon](
        [MaChiTiet] [int] IDENTITY(1,1) PRIMARY KEY,
        [MaHoaDon] [int] NOT NULL,
        [MaSach] [int] NOT NULL,
        [SoLuong] [int] NOT NULL DEFAULT 1,
        [DonGia] [decimal](18, 2) NOT NULL,
        [ThanhTien] [decimal](18, 2) NOT NULL,
        [GiamGia] [decimal](5, 2) DEFAULT 0,
        FOREIGN KEY ([MaHoaDon]) REFERENCES [HoaDon]([MaHoaDon]) ON DELETE CASCADE,
        FOREIGN KEY ([MaSach]) REFERENCES [Sach]([MaSach])
    );
END
GO

-- =====================================================
-- BẢNG: Đơn nhập hàng (Import Orders)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DonNhapHang]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[DonNhapHang](
        [MaDonNhap] [int] IDENTITY(1,1) PRIMARY KEY,
        [MaDonNhapString] [nvarchar](20) NOT NULL UNIQUE,
        [MaNhaCungCap] [int] NOT NULL,
        [MaNhanVien] [int] NOT NULL,
        [NgayNhap] [datetime] DEFAULT GETDATE(),
        [TongTien] [decimal](18, 2) DEFAULT 0,
        [GhiChu] [nvarchar](500),
        [TrangThai] [nvarchar](30) DEFAULT N'Đã nhập',
        [NgayTao] [datetime] DEFAULT GETDATE(),
        [NgayCapNhat] [datetime] DEFAULT GETDATE(),
        FOREIGN KEY ([MaNhaCungCap]) REFERENCES [NhaCungCap]([MaNhaCungCap]),
        FOREIGN KEY ([MaNhanVien]) REFERENCES [NhanVien]([MaNhanVien])
    );
END
GO

-- =====================================================
-- BẢNG: Chi tiết đơn nhập (Import Details)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ChiTietDonNhap]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[ChiTietDonNhap](
        [MaChiTiet] [int] IDENTITY(1,1) PRIMARY KEY,
        [MaDonNhap] [int] NOT NULL,
        [MaSach] [int] NOT NULL,
        [SoLuong] [int] NOT NULL DEFAULT 1,
        [DonGia] [decimal](18, 2) NOT NULL,
        [ThanhTien] [decimal](18, 2) NOT NULL,
        FOREIGN KEY ([MaDonNhap]) REFERENCES [DonNhapHang]([MaDonNhap]) ON DELETE CASCADE,
        FOREIGN KEY ([MaSach]) REFERENCES [Sach]([MaSach])
    );
END
GO

-- =====================================================
-- THÊM DỮ LIỆU MẪU (SAMPLE DATA)
-- =====================================================

-- Thêm nhân viên mẫu
IF NOT EXISTS (SELECT * FROM NhanVien)
BEGIN
    INSERT INTO NhanVien (HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, DiaChi, ChucVu, Luong, NgayVaoLam, TrangThai)
    VALUES
    (N'Nguyễn Văn A', N'Nam', '1990-01-01', '0912345678', 'nva@bookstore.com', N'Hà Nội', N'Admin', 15000000, '2020-01-01', N'Hoạt động'),
    (N'Trần Thị B', N'Nữ', '1995-05-15', '0923456789', 'ttb@bookstore.com', N'Hà Nội', N'Nhân viên', 8000000, '2021-06-01', N'Hoạt động'),
    (N'Lê Văn C', N'Nam', '1992-08-20', '0934567890', 'lvc@bookstore.com', N'TP.HCM', N'Nhân viên', 8000000, '2021-09-01', N'Hoạt động');
END
GO

-- Thêm tài khoản mẫu (mật khẩu: 123456 - đã mã hóa MD5)
IF NOT EXISTS (SELECT * FROM TaiKhoan)
BEGIN
    INSERT INTO TaiKhoan (TenDangNhap, MatKhau, VaiTro, MaNhanVien, TrangThai)
    VALUES
    ('admin', 'e10adc3949ba59abbe56e057f20f883e', 'Admin', 1, N'Hoạt động'), -- 123456
    ('nhanvien', 'e10adc3949ba59abbe56e057f20f883e', 'Nhân viên', 2, N'Hoạt động'); -- 123456
END
GO

-- Thêm khách hàng mẫu
IF NOT EXISTS (SELECT * FROM KhachHang)
BEGIN
    INSERT INTO KhachHang (HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, DiaChi, DiemTichLuy)
    VALUES
    (N'Phạm Thị D', N'Nữ', '1998-03-10', '0945678901', 'ptd@email.com', N'Hà Nội', 100),
    (N'Hoàng Văn E', N'Nam', '1995-11-25', '0956789012', 'hve@email.com', N'TP.HCM', 50),
    (N'Ngô Thị F', N'Nữ', '2000-07-18', '0967890123', 'ntf@email.com', N'Đà Nẵng', 0);
END
GO

-- Thêm nhà cung cấp mẫu
IF NOT EXISTS (SELECT * FROM NhaCungCap)
BEGIN
    INSERT INTO NhaCungCap (TenNhaCungCap, DiaChi, SoDienThoai, Email, NguoiLienHe)
    VALUES
    (N'Nhà xuất bản Trẻ', N'TP.HCM', '02838222222', 'nxbtre@nxbtre.vn', N'Nguyễn Văn X'),
    (N'Nhà xuất bản Giáo dục', N'Hà Nội', '02438221111', 'nxbgd@nxbgd.vn', N'Trần Thị Y'),
    (N'Nhà xuất bản Kim Đồng', N'TP.HCM', '02838303330', 'nxbkd@nxbkd.vn', N'Lê Văn Z');
END
GO

-- Thêm sách mẫu
IF NOT EXISTS (SELECT * FROM Sach)
BEGIN
    INSERT INTO Sach (TenSach, TacGia, NhaXuatBan, TheLoai, NamXuatBan, GiaBia, SoLuongTon, MoTa)
    VALUES
    (N'Lập trình Java cơ bản', N'Nguyễn Văn A', N'Nhà xuất bản Trẻ', N'Công nghệ', 2023, 89000, 100, N'Sách dành cho người mới học Java'),
    (N'Python cho người mới bắt đầu', N'Trần Văn B', N'Nhà xuất bản Giáo dục', N'Công nghệ', 2023, 79000, 150, N'Học Python từ cơ bản đến nâng cao'),
    (N'Tiếng Anh giao tiếp', N'Lê Thị C', N'Nhà xuất bản Đại học Quốc gia', N'Ngoại ngữ', 2022, 65000, 200, N'Tiếng Anh thực hành cho người đi làm'),
    (N'Toán học cao cấp tập 1', N'Phạm Văn D', N'Nhà xuất bản Giáo dục', N'Giáo khoa', 2021, 120000, 80, N'Sách giáo trình đại học'),
    (N'Lịch sử Việt Nam', N'Ngô Văn E', N'Nhà xuất bản Kim Đồng', N'Lịch sử', 2020, 95000, 50, N'Tổng quan lịch sử Việt Nam'),
    (N'Kinh doanh thời 4.0', N'Hoàng Thị F', N'Nhà xuất bản Tài chính', N'Kinh tế', 2023, 150000, 60, N'Chiến lược kinh doanh hiện đại'),
    (N'Thiet ke đồ họa', N'Vũ Văn G', N'Nhà xuất bản Mỹ thuật', N'Mỹ thuật', 2022, 180000, 40, N'Học thiết kế đồ họa với Photoshop'),
    (N'Nấu ăn ngon mỗi ngày', N'Đặng Thị H', N'Nhà xuất bản Ẩm thực', N'Ẩm thực', 2023, 75000, 120, N'Công thức nấu ăn gia đình');
END
GO

-- =====================================================
-- TẠO INDEXES ĐỂ TĂNG HIỆU SUẤT
-- =====================================================
CREATE INDEX idx_sach_ten ON Sach(TenSach);
CREATE INDEX idx_sach_theloai ON Sach(TheLoai);
CREATE INDEX idx_khachhang_ten ON KhachHang(HoTen);
CREATE INDEX idx_hoadon_ngay ON HoaDon(NgayLap);
CREATE INDEX idx_hoadon_makh ON HoaDon(MaKhachHang);
GO

PRINT N'Database created successfully!';
