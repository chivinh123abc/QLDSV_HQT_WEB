package com.ptithcm.shared.constants;

public final class CacheConstant {
    // Ngăn khởi tạo đối tượng
    private CacheConstant() {
    }

    // Thời gian sống mặc định của Cache (TTL - 24 giờ tính bằng giây)
    public static final long DEFAULT_TTL_SECONDS = 24 * 60 * 60;

    // Các Cache Keys hệ thống
    public static final String FACULTY_ALL = "sys:faculty:all";
    public static final String SUBJECT_ALL = "sys:subject:all";
    public static final String CLASSROOM_ALL = "sys:classroom:all";
    public static final String LECTURER_ALL = "sys:lecturer:all";

    // Redis Registration Queue và Status keys
    public static final String QUEUE_REGISTRATION = "QUEUE_DANG_KY";
    public static final String REG_STATUS_KEY_PREFIX = "reg_status:";
    public static final int REGISTRATION_STATUS_TTL_SECONDS = 600; // 10 minutes

    // Khóa động (Ví dụ nếu muốn cache theo điều kiện lọc cụ thể)
    public static String getClassroomByFacultyKey(String maKhoa) {
        return "sys:classroom:faculty:" + maKhoa;
    }

    /**
     * Helper to build registration status Redis key
     */
    public static String getRegStatusKey(String maSV, String maLTC) {
        return REG_STATUS_KEY_PREFIX + maSV.trim() + ":" + maLTC.trim();
    }
}
