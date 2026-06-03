package com.ptithcm.modules.mark.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public class SaveMarkDTO {

    @NotBlank(message = "Mã lớp tín chỉ không được để trống!")
    private String maLTC;

    @NotBlank(message = "Mã sinh viên không được để trống!")
    private String maSV;

    @DecimalMin(value = "0.0", message = "Điểm chuyên cần phải từ 0.0 đến 10.0.")
    @DecimalMax(value = "10.0", message = "Điểm chuyên cần phải từ 0.0 đến 10.0.")
    private Float diemCC;

    @DecimalMin(value = "0.0", message = "Điểm giữa kỳ phải từ 0.0 đến 10.0.")
    @DecimalMax(value = "10.0", message = "Điểm giữa kỳ phải từ 0.0 đến 10.0.")
    private Float diemGK;

    @DecimalMin(value = "0.0", message = "Điểm cuối kỳ phải từ 0.0 đến 10.0.")
    @DecimalMax(value = "10.0", message = "Điểm cuối kỳ phải từ 0.0 đến 10.0.")
    private Float diemCK;

    public String getMaLTC() {
        return maLTC;
    }

    public void setMaLTC(String maLTC) {
        this.maLTC = maLTC;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public Float getDiemCC() {
        return diemCC;
    }

    public void setDiemCC(Float diemCC) {
        this.diemCC = diemCC;
    }

    public Float getDiemGK() {
        return diemGK;
    }

    public void setDiemGK(Float diemGK) {
        this.diemGK = diemGK;
    }

    public Float getDiemCK() {
        return diemCK;
    }

    public void setDiemCK(Float diemCK) {
        this.diemCK = diemCK;
    }
}
