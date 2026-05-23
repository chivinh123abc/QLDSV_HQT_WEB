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
public abstract class AuditWithTimezone {

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist // Chạy ngay trước khi DAO gọi hàm save() / insert
    protected void onCreate() {
        this.createdAt = DateUtil.nowVn();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate // Chạy ngay trước khi DAO gọi hàm update() / merge()
    protected void onUpdate() {
        this.updatedAt = DateUtil.nowVn();
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
