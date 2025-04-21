package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "scope")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "created_at", nullable = true, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "scope")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegisteredClientScope> registeredClientScopes = new HashSet<>();

    public void addRegisteredClientScope(RegisteredClientScope registeredClientScope) {
        registeredClientScopes.add(registeredClientScope);
    }
}
