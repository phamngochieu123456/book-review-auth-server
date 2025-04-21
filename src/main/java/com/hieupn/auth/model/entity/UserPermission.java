package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_permissions")
@Data
@NoArgsConstructor
@org.hibernate.annotations.Immutable
public class UserPermission {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPermissionId implements Serializable {
        @Column(name = "user_id")
        private Integer userId;

        @Column(name = "permission_id")
        private Integer permissionId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserPermissionId id = (UserPermissionId) o;
            return Objects.equals(userId, id.userId) &&
                    Objects.equals(permissionId, id.permissionId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, permissionId);
        }
    }

    @EmbeddedId
    private UserPermissionId userPermissionId = new UserPermissionId();

    @Column(name = "assigned_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by", updatable = false)
    private Integer assignedBy;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "permission_id",
            insertable = false, updatable = false)
    private Permission permission;

    public UserPermission(Integer assignedBy, User user, Permission permission) {
        // Set fields
        this.assignedBy = assignedBy;
        this.user = user;
        this.permission = permission;

        // Set identifier values
        this.userPermissionId.userId = user.getId();
        this.userPermissionId.permissionId = permission.getId();

        // Guarantee referential integrity if made bidirectional
        user.addUserPermission(this);
        permission.addUserPermission(this);
    }
}
