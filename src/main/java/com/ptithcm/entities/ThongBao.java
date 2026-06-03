package com.ptithcm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.ptithcm.entities.base.LuuVetThoiGian;

@Entity
@Table(name = "thong_bao")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ThongBao extends LuuVetThoiGian {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "noi_dung", nullable = false)
    private String noiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao", referencedColumnName = "ten_dang_nhap", nullable = false)
    private TaiKhoan nguoiTao;

    @Version
    @Column(name = "version")
    private Integer version = 0;

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

    public TaiKhoan getNguoiTao() {
        return nguoiTao;
    }

    public void setNguoiTao(TaiKhoan nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Helper to return formatted date for JSP view
     */
    public String getNgayTaoFormatted() {
        if (getNgayTao() == null) {
            return "";
        }
        return getNgayTao().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
    }
}
