package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "role_permissions")
@Data
@NoArgsConstructor
@org.hibernate.annotations.Immutable
public class RolePermission {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RolePermissionId implements Serializable {
        @Column(name = "role_id")
        private Integer roleId;

        @Column(name = "permission_id")
        private Integer permissionId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RolePermissionId id = (RolePermissionId) o;
            return Objects.equals(roleId, id.roleId) &&
                    Objects.equals(permissionId, id.permissionId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roleId, permissionId);
        }
    }

    @EmbeddedId
    private RolePermissionId rolePermissionId = new RolePermissionId();

    @Column(name = "assigned_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by", updatable = false)
    private Integer assignedBy;

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(
            name = "permission_id",
            insertable = false, updatable = false)
    private Permission permission;

    public RolePermission(Integer assignedBy, Role role, Permission permission) {
        // Set fields
        this.assignedBy = assignedBy;
        this.role = role;
        this.permission = permission;

        // Set identifier values
        this.rolePermissionId.roleId = role.getId();
        this.rolePermissionId.permissionId = permission.getId();

        // Guarantee referential integrity if made bidirectional
        role.addRolePermission(this);
        permission.addRolePermission(this);
    }
}
