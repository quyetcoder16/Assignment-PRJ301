package quyet.leavemanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import quyet.leavemanagement.backend.configuration.SecurityUtils;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "user_created", updatable = false)
    Long userCreated;  // Lưu user_id thay vì User entity

    @LastModifiedBy
    @Column(name = "user_updated")
    Long userUpdated;

    @PrePersist
    public void prePersist() {
        this.userCreated = SecurityUtils.getCurrentUserId();
        this.userUpdated = this.userCreated;
    }

    @PreUpdate
    public void preUpdate() {
        this.userUpdated = SecurityUtils.getCurrentUserId();
    }
}
