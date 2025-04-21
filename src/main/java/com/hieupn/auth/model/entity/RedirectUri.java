package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "redirect_uri")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedirectUri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uri", nullable = false, unique = true, length = 200)
    private String uri;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "redirectUri")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegisteredClientRedirectUri> registeredClientRedirectUris = new HashSet<>();

    public void addRegisteredClientRedirectUri(RegisteredClientRedirectUri redirectUri) {
        registeredClientRedirectUris.add(redirectUri);
    }
}
