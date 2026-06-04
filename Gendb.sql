USE [master]
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'QLDSV_HTC_WEB')
BEGIN
    ALTER DATABASE [QLDSV_HTC_WEB] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [QLDSV_HTC_WEB];
END
GO

CREATE DATABASE [QLDSV_HTC_WEB]
GO

USE [QLDSV_HTC_WEB]
GO

-- ------------------------------------------------------------
-- CREATE TABLES
-- ------------------------------------------------------------

CREATE TABLE [dbo].[khoa] (
    [id] NVARCHAR(10) NOT NULL,
    [ten_khoa] NVARCHAR(100) NOT NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

CREATE TABLE [dbo].[lop] (
    [id] NVARCHAR(10) NOT NULL,
    [ten_lop] NVARCHAR(100) NULL,
    [khoa_hoc] NVARCHAR(20) NULL,
    [khoa_id] NVARCHAR(10) NOT NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

CREATE TABLE [dbo].[mon_hoc] (
    [id] NVARCHAR(10) NOT NULL,
    [ten_mon_hoc] NVARCHAR(100) NULL,
    [so_tiet_ly_thuyet] INT NULL,
    [so_tiet_thuc_hanh] INT NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

CREATE TABLE [dbo].[sinh_vien] (
    [id] NVARCHAR(10) NOT NULL,
    [ho] NVARCHAR(50) NULL,
    [ten] NVARCHAR(50) NULL,
    [phai] NVARCHAR(10) NULL,
    [dia_chi] NVARCHAR(200) NULL,
    [ngay_sinh] DATE NULL,
    [lop_id] NVARCHAR(10) NULL,
    [trang_thai_hoc] NVARCHAR(20) NOT NULL DEFAULT 'DANG_HOC',
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    [version] INT NOT NULL DEFAULT 0,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

CREATE TABLE [dbo].[giang_vien] (
    [id] NVARCHAR(10) NOT NULL,
    [khoa_id] NVARCHAR(10) NULL,
    [ho] NVARCHAR(50) NULL,
    [ten] NVARCHAR(50) NULL,
    [hoc_vi] NVARCHAR(50) NULL,
    [hoc_ham] NVARCHAR(50) NULL,
    [chuyen_mon] NVARCHAR(100) NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    [version] INT NOT NULL DEFAULT 0,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

CREATE TABLE [dbo].[lop_tin_chi] (
    [id] NVARCHAR(50) NOT NULL,
    [nien_khoa] NVARCHAR(20) NULL,
    [hoc_ky] INT NULL,
    [mon_hoc_id] NVARCHAR(10) NULL,
    [nhom] INT NULL,
    [giang_vien_id] NVARCHAR(10) NULL,
    [khoa_id] NVARCHAR(10) NULL,
    [so_sv_toi_thieu] INT NULL,
    [so_sv_toi_da] INT NULL,
    [trang_thai_lop] NVARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG',
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

CREATE TABLE [dbo].[dang_ky] (
    [lop_tin_chi_id] NVARCHAR(50) NOT NULL,
    [sinh_vien_id] NVARCHAR(10) NOT NULL,
    [diem_chuyen_can] FLOAT NULL,
    [diem_giua_ky] FLOAT NULL,
    [diem_cuoi_ky] FLOAT NULL,
    [trang_thai_dang_ky] NVARCHAR(20) NOT NULL DEFAULT 'HIEU_LUC',
    [da_thanh_toan] BIT NOT NULL DEFAULT 0,
    [ngay_thanh_toan] DATETIMEOFFSET NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    PRIMARY KEY CLUSTERED ([lop_tin_chi_id] ASC, [sinh_vien_id] ASC)
);

CREATE TABLE [dbo].[tai_khoan] (
    [ten_dang_nhap] NVARCHAR(50) NOT NULL,
    [mat_khau] NVARCHAR(255) NOT NULL,
    [email] NVARCHAR(100) NOT NULL,
    [phan_quyen] NVARCHAR(20) NOT NULL,
    [trang_thai] NVARCHAR(20) NOT NULL DEFAULT 'CHUA_KICH_HOAT',
    [avatar] NVARCHAR(255) NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    [version] INT NOT NULL DEFAULT 0,
    PRIMARY KEY CLUSTERED ([ten_dang_nhap] ASC)
);

-- ------------------------------------------------------------
-- INSERT SAMPLE DATA
-- ------------------------------------------------------------

-- Khoa
INSERT [dbo].[khoa] ([id], [ten_khoa]) VALUES (N'CNTT', N'Công nghệ thông tin')
INSERT [dbo].[khoa] ([id], [ten_khoa]) VALUES (N'ATTT', N'An toàn thông tin')
INSERT [dbo].[khoa] ([id], [ten_khoa]) VALUES (N'KT', N'Kinh tế')
INSERT [dbo].[khoa] ([id], [ten_khoa]) VALUES (N'VT', N'Viễn thông')

-- Lop
INSERT [dbo].[lop] ([id], [ten_lop], [khoa_hoc], [khoa_id]) VALUES (N'CNTT1', N'Lớp CNTT Mot1231', N'2022-2023', N'CNTT')
INSERT [dbo].[lop] ([id], [ten_lop], [khoa_hoc], [khoa_id]) VALUES (N'CNTT2', N'Lớp CNTT 2', N'2022-2023', N'CNTT')
INSERT [dbo].[lop] ([id], [ten_lop], [khoa_hoc], [khoa_id]) VALUES (N'D22CQCN01', N'CNTT Khóa 22 - Nhóm 1', N'2022-2023', N'CNTT')
INSERT [dbo].[lop] ([id], [ten_lop], [khoa_hoc], [khoa_id]) VALUES (N'D23CQAT01', N'ATTT Khóa 23 - Nhóm 1', N'2023-2024', N'ATTT')

-- Mon hoc
INSERT [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'CSDL', N'Cơ sở dữ liệu', 30, 15)
INSERT [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'CTDL', N'Cấu trúc dữ liệu và Giải thuật', 30, 30)
INSERT [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'MH01', N'Cấu trúc dữ liệu', 30, 15)
INSERT [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'MH02', N'Cơ sở dữ liệu fake', 30, 15)

-- Sinh vien
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'N22DCCN001', N'Nguyễn Văn', N'An', N'Nam', N'Hà Nội', CAST(N'2004-05-15' AS Date), N'D22CQCN01', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'N22DCCN002', N'Trần Thị', N'Bình', N'Nữ', N'TP HCM', CAST(N'2004-09-20' AS Date), N'D22CQCN01', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'N23DCAT001', N'Lê Văn', N'Cường', N'Nam', N'Đà Nẵng', CAST(N'2005-01-10' AS Date), N'D23CQAT01', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'SV01', N'Nguyễn', N'A', N'Nam', N'HCM', CAST(N'2003-01-01' AS Date), N'CNTT1', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'SV02', N'Trần', N'B', N'Nữ', N'Hà Nội', CAST(N'2003-05-10' AS Date), N'CNTT1', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'SV03', N'Lê', N'C', N'Nam', N'Đà Nẵng', CAST(N'2003-07-20' AS Date), N'CNTT2', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'SV045', N'Nguyễn', N'A', N'Nam', N'HCM', CAST(N'2003-01-01' AS Date), N'CNTT1', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'N23DCCN003', N'Phan Tuan Quoc', N'Anh', N'Nam', N'HCM', CAST(N'2005-01-01' AS Date), N'CNTT1', N'DANG_HOC')
INSERT [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) VALUES (N'N23DCCN067', N'Luong Chi', N'Vinh', N'Nam', N'An Giang', CAST(N'2005-01-01' AS Date), N'CNTT1', N'DANG_HOC')

-- Giang vien
INSERT [dbo].[giang_vien] ([id], [khoa_id], [ho], [ten], [hoc_vi], [hoc_ham], [chuyen_mon]) VALUES (N'GV01', N'CNTT', N'Nguyễn', N'An', N'TS', N'PGS', N'Lập trình')
INSERT [dbo].[giang_vien] ([id], [khoa_id], [ho], [ten], [hoc_vi], [hoc_ham], [chuyen_mon]) VALUES (N'GV02', N'CNTT', N'Trần', N'Bình', N'ThS', NULL, N'Cơ sở dữ liệu')

-- Lop tin chi
INSERT [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) VALUES (N'1', N'2025-2026', 1, N'MH01', 1, N'GV01', N'CNTT', 10, 80, N'HOAT_DONG')
INSERT [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) VALUES (N'2', N'2025-2026', 1, N'MH02', 1, N'GV02', N'CNTT', 10, 80, N'HOAT_DONG')
INSERT [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) VALUES (N'3', N'2025-2026', 1, N'CSDL', 1, N'GV01', N'ATTT', 10, 80, N'HOAT_DONG')
INSERT [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) VALUES (N'5', N'2025-2026', 2, N'CSDL', 2, N'GV01', N'CNTT', 10, 80, N'HOAT_DONG')
INSERT [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) VALUES (N'6', N'2025-2026', 1, N'CTDL', 1, N'GV01', N'CNTT', 1, 80, N'HOAT_DONG')
INSERT [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) VALUES (N'7', N'2025-2026', 1, N'MH01', 2, N'GV02', N'CNTT', 10, 80, N'HOAT_DONG')

-- Dang ky
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'1', N'N23DCAT001', NULL, NULL, NULL, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'1', N'SV01', 8, 7, 9, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'1', N'SV02', 7, 6, 8, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'2', N'SV01', NULL, NULL, NULL, N'DA_HUY')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'2', N'SV03', 9, 8, 9, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'3', N'N23DCAT001', NULL, NULL, NULL, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'3', N'SV01', 5.1, 7, 8, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'3', N'SV02', NULL, NULL, NULL, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'5', N'SV01', 6, 4, 6, N'HIEU_LUC')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'5', N'SV02', NULL, NULL, NULL, N'DA_HUY')
INSERT [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky]) VALUES (N'6', N'SV01', NULL, NULL, NULL, N'DA_HUY')

-- Centralized Tai Khoan (BCrypt hash of '123' is $2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja)
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'admin', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'admin@student.ptithcm.edu.vn', N'PGV', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'GV01', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'gv01@student.ptithcm.edu.vn', N'PGV', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'GV02', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'gv02@student.ptithcm.edu.vn', N'KHOA', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'SV01', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv01@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'SV02', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv02@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'SV03', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv03@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'N22DCCN001', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'n22dccn001@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'N22DCCN002', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'n22dccn002@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'N23DCAT001', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'n23dcat001@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'N23DCCN003', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'n23dccn003@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')
INSERT [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) VALUES (N'N23DCCN067', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'n23dccn067@student.ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT')

-- ------------------------------------------------------------
-- CONSTRAINTS & FOREIGN KEYS
-- ------------------------------------------------------------

ALTER TABLE [dbo].[lop] WITH CHECK ADD CONSTRAINT [FK_lop_khoa] FOREIGN KEY([khoa_id])
REFERENCES [dbo].[khoa] ([id]);

ALTER TABLE [dbo].[sinh_vien] WITH CHECK ADD CONSTRAINT [FK_sinh_vien_lop] FOREIGN KEY([lop_id])
REFERENCES [dbo].[lop] ([id]);

ALTER TABLE [dbo].[giang_vien] WITH CHECK ADD CONSTRAINT [FK_giang_vien_khoa] FOREIGN KEY([khoa_id])
REFERENCES [dbo].[khoa] ([id]);

ALTER TABLE [dbo].[lop_tin_chi] WITH CHECK ADD CONSTRAINT [FK_lop_tin_chi_mon_hoc] FOREIGN KEY([mon_hoc_id])
REFERENCES [dbo].[mon_hoc] ([id]);

ALTER TABLE [dbo].[lop_tin_chi] WITH CHECK ADD CONSTRAINT [FK_lop_tin_chi_giang_vien] FOREIGN KEY([giang_vien_id])
REFERENCES [dbo].[giang_vien] ([id]);

ALTER TABLE [dbo].[lop_tin_chi] WITH CHECK ADD CONSTRAINT [FK_lop_tin_chi_khoa] FOREIGN KEY([khoa_id])
REFERENCES [dbo].[khoa] ([id]);

ALTER TABLE [dbo].[dang_ky] WITH CHECK ADD CONSTRAINT [FK_dang_ky_lop_tin_chi] FOREIGN KEY([lop_tin_chi_id])
REFERENCES [dbo].[lop_tin_chi] ([id]);

ALTER TABLE [dbo].[dang_ky] WITH CHECK ADD CONSTRAINT [FK_dang_ky_sinh_vien] FOREIGN KEY([sinh_vien_id])
REFERENCES [dbo].[sinh_vien] ([id]);

CREATE TABLE [dbo].[thong_bao] (
    [id] VARCHAR(36) NOT NULL,
    [tieu_de] NVARCHAR(255) NOT NULL,
    [noi_dung] NVARCHAR(MAX) NOT NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    [nguoi_tao] NVARCHAR(50) NOT NULL,
    [version] INT NOT NULL DEFAULT 0,
    PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_thong_bao_tai_khoan] FOREIGN KEY ([nguoi_tao]) REFERENCES [dbo].[tai_khoan] ([ten_dang_nhap])
);

CREATE TABLE [dbo].[thong_bao_da_doc] (
    [id_thong_bao] VARCHAR(36) NOT NULL,
    [ten_dang_nhap] NVARCHAR(50) NOT NULL,
    PRIMARY KEY CLUSTERED ([id_thong_bao] ASC, [ten_dang_nhap] ASC),
    CONSTRAINT [FK_da_doc_thong_bao] FOREIGN KEY ([id_thong_bao]) REFERENCES [dbo].[thong_bao] ([id]) ON DELETE CASCADE,
    CONSTRAINT [FK_da_doc_tai_khoan] FOREIGN KEY ([ten_dang_nhap]) REFERENCES [dbo].[tai_khoan] ([ten_dang_nhap]) ON DELETE CASCADE
);

USE [master]
GO
