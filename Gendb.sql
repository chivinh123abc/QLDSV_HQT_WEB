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

-- ============================================================================
-- 1. CREATE TABLES (NO FOREIGN KEY CONSTRAINTS TO SIMPLIFY INITIAL INSERTS)
-- ============================================================================

-- Table for Faculties (Khoa)
CREATE TABLE [dbo].[khoa] (
    [id] NVARCHAR(10) NOT NULL,
    [ten_khoa] NVARCHAR(100) NOT NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

-- Table for Student Classrooms (Lớp hành chính)
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

-- Table for Subjects (Môn học)
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

-- Table for Students (Sinh viên)
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

-- Table for Lecturers/Staff (Giảng viên)
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

-- Table for Credit Classes (Lớp tín chỉ)
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

-- Table for Accounts (Tài khoản)
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

-- Table for Class Registrations & Marks (Đăng ký)
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

-- Table for Announcements (Thông báo)
CREATE TABLE [dbo].[thong_bao] (
    [id] VARCHAR(36) NOT NULL,
    [tieu_de] NVARCHAR(255) NOT NULL,
    [noi_dung] NVARCHAR(MAX) NOT NULL,
    [ngay_tao] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_cap_nhat] DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    [ngay_xoa] DATETIMEOFFSET NULL,
    [nguoi_tao] NVARCHAR(50) NOT NULL,
    [version] INT NOT NULL DEFAULT 0,
    PRIMARY KEY CLUSTERED ([id] ASC)
);

-- Table for Read Announcements tracking (Thông báo đã đọc)
CREATE TABLE [dbo].[thong_bao_da_doc] (
    [id_thong_bao] VARCHAR(36) NOT NULL,
    [ten_dang_nhap] NVARCHAR(50) NOT NULL,
    PRIMARY KEY CLUSTERED ([id_thong_bao] ASC, [ten_dang_nhap] ASC)
);


-- ============================================================================
-- 2. INSERT SAMPLE SEED DATA
-- ============================================================================

-- --------------------------------------------------
-- Master Data: Faculties (Khoa)
-- --------------------------------------------------
-- Inserting 3 distinct faculties
INSERT INTO [dbo].[khoa] ([id], [ten_khoa]) VALUES (N'CNTT', N'Công nghệ thông tin');
INSERT INTO [dbo].[khoa] ([id], [ten_khoa]) VALUES (N'VT', N'Viễn thông');
INSERT INTO [dbo].[khoa] ([id], [ten_khoa]) VALUES (N'ATTT', N'An toàn thông tin');

-- --------------------------------------------------
-- Master Data: Classrooms (Lớp học)
-- --------------------------------------------------
-- Inserting 3 distinct classrooms matching different faculties
INSERT INTO [dbo].[lop] ([id], [ten_lop], [khoa_hoc], [khoa_id]) VALUES (N'D22CQCN01', N'Lớp Công nghệ thông tin 1 - K22', N'2022-2023', N'CNTT');
INSERT INTO [dbo].[lop] ([id], [ten_lop], [khoa_hoc], [khoa_id]) VALUES (N'D22CQVT01', N'Lớp Viễn thông 1 - K22', N'2022-2023', N'VT');
INSERT INTO [dbo].[lop] ([id], [ten_lop], [khoa_hoc], [khoa_id]) VALUES (N'D22CQAT01', N'Lớp An toàn thông tin 1 - K22', N'2022-2023', N'ATTT');

-- --------------------------------------------------
-- Master Data: Subjects (Môn học)
-- --------------------------------------------------
-- Inserting 5 distinct subjects with varying credits (ly thuyet & thuc hanh)
INSERT INTO [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'CSDL', N'Cơ sở dữ liệu', 30, 15);
INSERT INTO [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'CTDL', N'Cấu trúc dữ liệu và giải thuật', 30, 30);
INSERT INTO [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'OOP', N'Lập trình hướng đối tượng', 30, 15);
INSERT INTO [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'LTC', N'Lập trình C', 30, 15);
INSERT INTO [dbo].[mon_hoc] ([id], [ten_mon_hoc], [so_tiet_ly_thuyet], [so_tiet_thuc_hanh]) VALUES (N'HDH', N'Hệ điều hành', 30, 15);

-- --------------------------------------------------
-- Master Data: Lecturers/Staff (Giảng viên)
-- --------------------------------------------------
-- Inserting lecturers representing different faculties
INSERT INTO [dbo].[giang_vien] ([id], [khoa_id], [ho], [ten], [hoc_vi], [hoc_ham], [chuyen_mon]) VALUES (N'GV01', N'CNTT', N'Nguyễn Văn', N'An', N'TS', N'PGS', N'Khoa học máy tính');
INSERT INTO [dbo].[giang_vien] ([id], [khoa_id], [ho], [ten], [hoc_vi], [hoc_ham], [chuyen_mon]) VALUES (N'GV02', N'VT', N'Trần Thị', N'Bình', N'ThS', NULL, N'Mạng viễn thông');
INSERT INTO [dbo].[giang_vien] ([id], [khoa_id], [ho], [ten], [hoc_vi], [hoc_ham], [chuyen_mon]) VALUES (N'GV03', N'ATTT', N'Lê Hoàng', N'Minh', N'TS', NULL, N'An toàn hệ thống');

-- --------------------------------------------------
-- Master Data: Credit Classes (Lớp tín chỉ)
-- --------------------------------------------------
-- LTC01: Subject: CSDL, Lecturer: GV01 (CNTT), Year: 2025-2026, Semester: 1, Group: 1, Min: 10, Max: 80
INSERT INTO [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) 
VALUES (N'LTC01', N'2025-2026', 1, N'CSDL', 1, N'GV01', N'CNTT', 10, 80, N'HOAT_DONG');

-- LTC02: Subject: CTDL, Lecturer: GV01 (CNTT), Year: 2025-2026, Semester: 1, Group: 1, Min: 10, Max: 80
INSERT INTO [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) 
VALUES (N'LTC02', N'2025-2026', 1, N'CTDL', 1, N'GV01', N'CNTT', 10, 80, N'HOAT_DONG');

-- LTC03: Subject: LTC, Lecturer: GV02 (VT), Year: 2025-2026, Semester: 1, Group: 1, Min: 10, Max: 80
INSERT INTO [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) 
VALUES (N'LTC03', N'2025-2026', 1, N'LTC', 1, N'GV02', N'VT', 10, 80, N'HOAT_DONG');

-- LTC04: Subject: OOP (Scenario E: Full Class), Lecturer: GV01, Year: 2025-2026, Semester: 1, Group: 1, Min: 1, Max: 3
INSERT INTO [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) 
VALUES (N'LTC04', N'2025-2026', 1, N'OOP', 1, N'GV01', N'CNTT', 1, 3, N'HOAT_DONG');

-- LTC05: Subject: HDH (Scenario F: Incomplete Marks), Lecturer: GV02, Year: 2025-2026, Semester: 1, Group: 1, Min: 10, Max: 80
INSERT INTO [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) 
VALUES (N'LTC05', N'2025-2026', 1, N'HDH', 1, N'GV02', N'VT', 10, 80, N'HOAT_DONG');

-- LTC06: Subject: LTC (Scenario D: Ghost Class - Zero Registrations), Lecturer: GV03, Year: 2025-2026, Semester: 1, Group: 2, Min: 10, Max: 80
INSERT INTO [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) 
VALUES (N'LTC06', N'2025-2026', 1, N'LTC', 2, N'GV03', N'ATTT', 10, 80, N'HOAT_DONG');

-- LTC07: Subject: CSDL (Scenario C: Retake class), Lecturer: GV01, Year: 2025-2026, Semester: 2, Group: 1, Min: 10, Max: 80
INSERT INTO [dbo].[lop_tin_chi] ([id], [nien_khoa], [hoc_ky], [mon_hoc_id], [nhom], [giang_vien_id], [khoa_id], [so_sv_toi_thieu], [so_sv_toi_da], [trang_thai_lop]) 
VALUES (N'LTC07', N'2025-2026', 2, N'CSDL', 1, N'GV01', N'CNTT', 10, 80, N'HOAT_DONG');


-- --------------------------------------------------
-- Transactional Data: Students (Sinh viên)
-- --------------------------------------------------
-- Student SV01: Golden Student (CNTT)
INSERT INTO [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) 
VALUES (N'SV01', N'Nguyễn Thanh', N'Bình', N'Nam', N'Hà Nội', CAST(N'2004-05-15' AS Date), N'D22CQCN01', N'DANG_HOC');

-- Student SV02: The Debtor (CNTT)
INSERT INTO [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) 
VALUES (N'SV02', N'Lê Hoàng', N'Long', N'Nam', N'TP HCM', CAST(N'2004-09-20' AS Date), N'D22CQCN01', N'DANG_HOC');

-- Student SV03: The Failure/Retaker (CNTT)
INSERT INTO [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) 
VALUES (N'SV03', N'Phạm Minh', N'Tuấn', N'Nam', N'Đà Nẵng', CAST(N'2004-11-10' AS Date), N'D22CQCN01', N'DANG_HOC');

-- Student SV04: Incomplete Marks (VT)
INSERT INTO [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) 
VALUES (N'SV04', N'Nguyễn Thị', N'Mai', N'Nữ', N'Hải Phòng', CAST(N'2004-03-08' AS Date), N'D22CQVT01', N'DANG_HOC');

-- Student SV05: Class filler for Scenario E (ATTT)
INSERT INTO [dbo].[sinh_vien] ([id], [ho], [ten], [phai], [dia_chi], [ngay_sinh], [lop_id], [trang_thai_hoc]) 
VALUES (N'SV05', N'Trần Thu', N'Hà', N'Nữ', N'Cần Thơ', CAST(N'2004-07-22' AS Date), N'D22CQAT01', N'DANG_HOC');


-- --------------------------------------------------
-- Transactional Data: Class Registrations, Marks, & Payments
-- --------------------------------------------------

-- --- Scenario A (The Golden Student: SV01) ---
-- Registered, paid perfectly, high marks
INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC01', N'SV01', 10.0, 9.0, 9.5, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-10 08:30:00', '+07:00'));

INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC02', N'SV01', 10.0, 8.5, 9.0, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-10 08:30:00', '+07:00'));

INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC03', N'SV01', 9.0, 9.0, 8.5, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-10 08:30:00', '+07:00'));


-- --- Scenario B (The Debtor: SV02) ---
-- Registered, has grades, but tuition fee NOT paid (da_thanh_toan = 0, ngay_thanh_toan = NULL)
INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC01', N'SV02', 9.0, 8.0, 7.5, N'HIEU_LUC', 0, NULL);

INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC02', N'SV02', 8.0, 7.5, 8.0, N'HIEU_LUC', 0, NULL);


-- --- Scenario C (The Failure/Retaker: SV03) ---
-- Semester 1: Failed CSDL (Final grade < 4.0: 5*0.1 + 3*0.3 + 2*0.6 = 2.6)
INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC01', N'SV03', 5.0, 3.0, 2.0, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-12 09:15:00', '+07:00'));

-- Semester 2: Retaking CSDL (LTC07) (Grade is currently NULL)
INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC07', N'SV03', NULL, NULL, NULL, N'HIEU_LUC', 0, NULL);


-- --- Scenario D (The Ghost Class: LTC06) ---
-- No registrations exist for LTC06 (Empty by design)


-- --- Scenario E (The Full Class: LTC04, Max Capacity = 3) ---
-- Fill up LTC04 with exactly 3 students (SV01, SV02, SV05) to check capacity locking
INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC04', N'SV01', 9.0, 8.0, 8.5, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-10 10:00:00', '+07:00'));

INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC04', N'SV02', 8.5, 7.5, 9.0, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-11 11:30:00', '+07:00'));

INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC04', N'SV05', 9.0, 8.5, 8.0, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-12 14:20:00', '+07:00'));


-- --- Scenario F (Incomplete Marks: SV04) ---
-- Registered, paid, has CC & GK but CK (Final exam) mark is NULL
INSERT INTO [dbo].[dang_ky] ([lop_tin_chi_id], [sinh_vien_id], [diem_chuyen_can], [diem_giua_ky], [diem_cuoi_ky], [trang_thai_dang_ky], [da_thanh_toan], [ngay_thanh_toan])
VALUES (N'LTC05', N'SV04', 9.0, 8.0, NULL, N'HIEU_LUC', 1, TODATETIMEOFFSET('2025-09-10 09:45:00', '+07:00'));


-- --------------------------------------------------
-- User Accounts: Credentials (Tài khoản)
-- --------------------------------------------------
-- All accounts use the default password '123' (BCrypt hashed)
-- Hash: $2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja
-- Role definitions: PGV (Admin/Staff), KHOA (Staff/Lecturer), SINHVIEN (Student)

-- Admin account (PGV role)
INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'admin', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'admin@ptithcm.edu.vn', N'PGV', N'DA_KICH_HOAT');

-- Lecturer/Staff accounts (KHOA role for different faculties)
INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'GV01', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'gv01@ptithcm.edu.vn', N'KHOA', N'DA_KICH_HOAT');

INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'GV02', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'gv02@ptithcm.edu.vn', N'KHOA', N'DA_KICH_HOAT');

INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'GV03', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'gv03@ptithcm.edu.vn', N'KHOA', N'DA_KICH_HOAT');

-- Student accounts (SINHVIEN role)
INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'SV01', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv01@ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT');

INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'SV02', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv02@ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT');

INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'SV03', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv03@ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT');

INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'SV04', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv04@ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT');

INSERT INTO [dbo].[tai_khoan] ([ten_dang_nhap], [mat_khau], [email], [phan_quyen], [trang_thai]) 
VALUES (N'SV05', N'$2a$12$o63QUDMoGbNjRpIpkuRUDuicRWTNcZG0caYDyTQJ4jGbuAUTZbCja', N'sv05@ptithcm.edu.vn', N'SINHVIEN', N'DA_KICH_HOAT');


-- --------------------------------------------------
-- Master Data: Notifications (Thông báo)
-- --------------------------------------------------
-- Inserting 2 realistic announcements
INSERT INTO [dbo].[thong_bao] ([id], [tieu_de], [noi_dung], [nguoi_tao]) 
VALUES ('ANN01', N'Lịch đăng ký học phần Học kỳ 1 - Niên khóa 2025-2026', N'Phòng giáo vụ thông báo lịch đăng ký học phần trực tuyến dành cho toàn thể sinh viên bắt đầu từ ngày 15/09/2025.', N'admin');

INSERT INTO [dbo].[thong_bao] ([id], [tieu_de], [noi_dung], [nguoi_tao]) 
VALUES ('ANN02', N'Thông báo lịch thi kết thúc học phần Học kỳ 1', N'Giảng viên nhắc nhở sinh viên chuẩn bị ôn tập kỹ lưỡng. Lịch thi chi tiết sẽ được cập nhật tại cổng thông tin.', N'GV01');

-- Mock read status for notifications
INSERT INTO [dbo].[thong_bao_da_doc] ([id_thong_bao], [ten_dang_nhap]) VALUES ('ANN01', 'SV01');
INSERT INTO [dbo].[thong_bao_da_doc] ([id_thong_bao], [ten_dang_nhap]) VALUES ('ANN01', 'SV02');


-- ============================================================================
-- 3. CREATE FOREIGN KEY CONSTRAINTS
-- ============================================================================

-- Constraints for Class table (lop)
ALTER TABLE [dbo].[lop] WITH CHECK ADD CONSTRAINT [FK_lop_khoa] FOREIGN KEY([khoa_id])
REFERENCES [dbo].[khoa] ([id]);

-- Constraints for Student table (sinh_vien)
ALTER TABLE [dbo].[sinh_vien] WITH CHECK ADD CONSTRAINT [FK_sinh_vien_lop] FOREIGN KEY([lop_id])
REFERENCES [dbo].[lop] ([id]);

-- Constraints for Lecturer table (giang_vien)
ALTER TABLE [dbo].[giang_vien] WITH CHECK ADD CONSTRAINT [FK_giang_vien_khoa] FOREIGN KEY([khoa_id])
REFERENCES [dbo].[khoa] ([id]);

-- Constraints for Credit Class table (lop_tin_chi)
ALTER TABLE [dbo].[lop_tin_chi] WITH CHECK ADD CONSTRAINT [FK_lop_tin_chi_mon_hoc] FOREIGN KEY([mon_hoc_id])
REFERENCES [dbo].[mon_hoc] ([id]);

ALTER TABLE [dbo].[lop_tin_chi] WITH CHECK ADD CONSTRAINT [FK_lop_tin_chi_giang_vien] FOREIGN KEY([giang_vien_id])
REFERENCES [dbo].[giang_vien] ([id]);

ALTER TABLE [dbo].[lop_tin_chi] WITH CHECK ADD CONSTRAINT [FK_lop_tin_chi_khoa] FOREIGN KEY([khoa_id])
REFERENCES [dbo].[khoa] ([id]);

-- Constraints for Registration table (dang_ky)
ALTER TABLE [dbo].[dang_ky] WITH CHECK ADD CONSTRAINT [FK_dang_ky_lop_tin_chi] FOREIGN KEY([lop_tin_chi_id])
REFERENCES [dbo].[lop_tin_chi] ([id]);

ALTER TABLE [dbo].[dang_ky] WITH CHECK ADD CONSTRAINT [FK_dang_ky_sinh_vien] FOREIGN KEY([sinh_vien_id])
REFERENCES [dbo].[sinh_vien] ([id]);

-- Constraints for Notifications table (thong_bao)
ALTER TABLE [dbo].[thong_bao] WITH CHECK ADD CONSTRAINT [FK_thong_bao_tai_khoan] FOREIGN KEY([nguoi_tao]) 
REFERENCES [dbo].[tai_khoan] ([ten_dang_nhap]);

-- Constraints for Read Notifications tracking table (thong_bao_da_doc)
ALTER TABLE [dbo].[thong_bao_da_doc] WITH CHECK ADD CONSTRAINT [FK_da_doc_thong_bao] FOREIGN KEY([id_thong_bao]) 
REFERENCES [dbo].[thong_bao] ([id]) ON DELETE CASCADE;

ALTER TABLE [dbo].[thong_bao_da_doc] WITH CHECK ADD CONSTRAINT [FK_da_doc_tai_khoan] FOREIGN KEY([ten_dang_nhap]) 
REFERENCES [dbo].[tai_khoan] ([ten_dang_nhap]) ON DELETE CASCADE;

USE [master]
GO
