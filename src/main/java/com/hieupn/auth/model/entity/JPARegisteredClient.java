package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "registered_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JPARegisteredClient {

    @Id
    @Column(name = "id", length = 100)
    private String id;

    @Column(name = "client_id", nullable = false, unique = true, length = 100)
    private String clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "registeredClient")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegisteredClientAuthenticationMethod> authenticationMethods = new HashSet<>();

    @OneToMany(mappedBy = "registeredClient")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegisteredClientGrantType> grantTypes = new HashSet<>();

    @OneToMany(mappedBy = "registeredClient")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegisteredClientRedirectUri> redirectUris = new HashSet<>();

    @OneToMany(mappedBy = "registeredClient")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegisteredClientScope> scopes = new HashSet<>();

    public void addAuthenticationMethod(RegisteredClientAuthenticationMethod method) {
        authenticationMethods.add(method);
    }

    public void addGrantType(RegisteredClientGrantType grantType) {
        grantTypes.add(grantType);
    }

    public void addRedirectUri(RegisteredClientRedirectUri redirectUri) {
        redirectUris.add(redirectUri);
    }

    public void addScope(RegisteredClientScope scope) {
        scopes.add(scope);
    }
}
