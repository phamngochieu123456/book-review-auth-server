package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_roles")
@Data
@NoArgsConstructor
@org.hibernate.annotations.Immutable
public class UserRole {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoleId implements Serializable {
        @Column(name = "user_id")
        private Integer userId;

        @Column(name = "role_id")
        private Integer roleId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserRoleId id = (UserRoleId) o;
            return Objects.equals(userId, id.userId) &&
                    Objects.equals(roleId, id.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, roleId);
        }
    }

    @EmbeddedId
    private UserRoleId userRoleId = new UserRoleId();

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
            name = "role_id",
            insertable = false, updatable = false)
    private Role role;

    public UserRole(Integer assignedBy,
                    User user,
                    Role role) {

        // Set fields
        this.assignedBy = assignedBy;
        this.user = user;
        this.role = role;

        // Set identifier values
        this.userRoleId.userId = user.getId();
        this.userRoleId.roleId = role.getId();

        // Guarantee referential integrity if made bidirectional
        user.addUserRole(this);
        role.addUserRole(this);
    }
}
