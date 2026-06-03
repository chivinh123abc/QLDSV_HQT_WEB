package com.ptithcm.modules.announcement.dtos;

import jakarta.validation.constraints.NotBlank;

public class AnnouncementDTO {

    private String id;

    @NotBlank(message = "Tiêu đề thông báo không được để trống!")
    private String tieuDe;

    @NotBlank(message = "Nội dung thông báo không được để trống!")
    private String noiDung;

    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
