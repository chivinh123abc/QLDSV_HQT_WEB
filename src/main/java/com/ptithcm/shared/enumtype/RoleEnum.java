package com.ptithcm.shared.enumtype;

public enum RoleEnum {
    PGV(1, "PGV"), KHOA(2, "KHOA"), SINHVIEN(3, "SINHVIEN");

    private final int id;
    private final String code;

    RoleEnum(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public static RoleEnum getById(int id) {
        for (RoleEnum role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        return null;
    }

    public static RoleEnum getByCode(String code) {
        for (RoleEnum role : values()) {
            if (role.code.equalsIgnoreCase(code)) {
                return role;
            }
        }
        return null;
    }
}
