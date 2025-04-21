package com.hieupn.auth.model.entity;

import com.hieupn.auth.converter.StatusEnumConverter;
import com.hieupn.auth.model.enums.UserStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Convert(converter = StatusEnumConverter.class)
    @Column(name = "status", nullable = false, length = 50)
    private UserStatusType status;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserPermission> userPermissions = new HashSet<>();

    public void addUserRole(UserRole userRole) {
        userRoles.add(userRole);
    }

    public void addUserPermission(UserPermission userPermission) {
        userPermissions.add(userPermission);
    }
}
