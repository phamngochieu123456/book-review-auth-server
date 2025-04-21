package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "registered_client_grant_type")
@Data
@NoArgsConstructor
@org.hibernate.annotations.Immutable
public class RegisteredClientGrantType {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisteredClientGrantTypeId implements Serializable {
        @Column(name = "registered_client_id", length = 100)
        private String registeredClientId;

        @Column(name = "grant_type_id")
        private Integer grantTypeId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RegisteredClientGrantTypeId id = (RegisteredClientGrantTypeId) o;
            return Objects.equals(registeredClientId, id.registeredClientId) &&
                    Objects.equals(grantTypeId, id.grantTypeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registeredClientId, grantTypeId);
        }
    }

    @EmbeddedId
    private RegisteredClientGrantTypeId id = new RegisteredClientGrantTypeId();

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
            name = "grant_type_id",
            insertable = false, updatable = false)
    private AuthorizationGrantType grantType;

    public RegisteredClientGrantType(Integer assignedBy, JPARegisteredClient registeredClient, AuthorizationGrantType grantType) {
        this.assignedBy = assignedBy;
        this.registeredClient = registeredClient;
        this.grantType = grantType;

        this.id.registeredClientId = registeredClient.getId();
        this.id.grantTypeId = grantType.getId();

        registeredClient.addGrantType(this);
        grantType.addRegisteredClientGrantType(this);
    }
}
