package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authorization_grant_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationGrantType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "grantType")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegisteredClientGrantType> registeredClientGrantTypes = new HashSet<>();

    public void addRegisteredClientGrantType(RegisteredClientGrantType registeredClientGrantType) {
        registeredClientGrantTypes.add(registeredClientGrantType);
    }
}
