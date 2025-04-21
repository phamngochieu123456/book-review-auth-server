package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "registered_client_redirect_uri")
@Data
@NoArgsConstructor
@org.hibernate.annotations.Immutable
public class RegisteredClientRedirectUri {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisteredClientRedirectUriId implements Serializable {
        @Column(name = "registered_client_id", length = 100)
        private String registeredClientId;

        @Column(name = "redirect_uri_id")
        private Integer redirectUriId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RegisteredClientRedirectUriId id = (RegisteredClientRedirectUriId) o;
            return Objects.equals(registeredClientId, id.registeredClientId) &&
                    Objects.equals(redirectUriId, id.redirectUriId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registeredClientId, redirectUriId);
        }
    }

    @EmbeddedId
    private RegisteredClientRedirectUriId id = new RegisteredClientRedirectUriId();

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "assigned_by", updatable = false)
    private Integer assignedBy;

    @ManyToOne
    @JoinColumn(
            name = "registered_client_id",
            insertable = false, updatable = false)
    private JPARegisteredClient registeredClient;

    @ManyToOne
    @JoinColumn(
            name = "redirect_uri_id",
            insertable = false, updatable = false)
    private RedirectUri redirectUri;

    public RegisteredClientRedirectUri(Integer assignedBy, JPARegisteredClient registeredClient, RedirectUri redirectUri) {
        this.assignedBy = assignedBy;
        this.registeredClient = registeredClient;
        this.redirectUri = redirectUri;

        this.id.registeredClientId = registeredClient.getId();
        this.id.redirectUriId = redirectUri.getId();

        registeredClient.addRedirectUri(this);
        redirectUri.addRegisteredClientRedirectUri(this);
    }
}
