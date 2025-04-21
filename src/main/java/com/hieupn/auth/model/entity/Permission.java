package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer id;

    @Column(name = "permission_name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "permission")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserPermission> userPermissions = new HashSet<>();

    @OneToMany(mappedBy = "permission")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RolePermission> rolePermissions = new HashSet<>();

    public void addUserPermission(UserPermission userPermission) {
        userPermissions.add(userPermission);
    }

    public void addRolePermission(RolePermission rolePermission) {
        rolePermissions.add(rolePermission);
    }
}
