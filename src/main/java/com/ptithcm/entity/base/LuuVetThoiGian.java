package com.ptithcm.entity.base;

import java.time.OffsetDateTime;
import org.hibernate.annotations.SQLRestriction;
import com.ptithcm.shared.util.DateUtil;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

// Annotation này BẮT BUỘC PHẢI CÓ:
// Báo cho Hibernate biết đây không phải là 1 bảng độc lập
@MappedSuperclass
@SQLRestriction("deleted_at IS NULL")
public abstract class LuuVetThoiGian {

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime ngayTao;

    @Column(name = "updated_at")
    private OffsetDateTime ngayCapNhat;

    @Column(name = "deleted_at")
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
