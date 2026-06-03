package com.ptithcm.modules.registration.dtos;

import jakarta.validation.constraints.NotBlank;

public class CourseRegistrationDTO {

    @NotBlank(message = "Mã lớp tín chỉ không được để trống!")
    private String maLTC;

    public String getMaLTC() {
        return maLTC;
    }

    public void setMaLTC(String maLTC) {
        this.maLTC = maLTC;
    }
}
