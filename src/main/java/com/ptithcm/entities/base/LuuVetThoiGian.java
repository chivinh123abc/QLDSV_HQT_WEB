package com.ptithcm.entities.base;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.hibernate.annotations.SQLRestriction;

import com.ptithcm.shared.utils.DateUtil;

// Annotation này BẮT BUỘC PHẢI CÓ:
// Báo cho Hibernate biết đây không phải là 1 bảng độc lập
@MappedSuperclass
@SQLRestriction("ngay_xoa IS NULL")
public abstract class LuuVetThoiGian {

    @Column(name = "ngay_tao", updatable = false)
    private OffsetDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

    @Column(name = "ngay_xoa")
    private OffsetDateTime ngayXoa;

    @PrePersist // Chạy ngay trước khi DAO gọi hàm save() / insert
    protected void onCreate() {
        this.ngayTao = DateUtil.nowVn();
        this.ngayCapNhat = this.ngayTao;
    }

    @PreUpdate // Chạy ngay trước khi DAO gọi hàm update() / merge()
    protected void onUpdate() {
        this.ngayCapNhat = DateUtil.nowVn();
    }

    public OffsetDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(OffsetDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public OffsetDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(OffsetDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public OffsetDateTime getNgayXoa() {
        return ngayXoa;
    }

    public void setNgayXoa(OffsetDateTime ngayXoa) {
        this.ngayXoa = ngayXoa;
    }
}
