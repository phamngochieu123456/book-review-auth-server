package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "registered_client_scope")
@Data
@NoArgsConstructor
@org.hibernate.annotations.Immutable
public class RegisteredClientScope {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisteredClientScopeId implements Serializable {
        @Column(name = "registered_client_id", length = 100)
        private String registeredClientId;

        @Column(name = "scope_id")
        private Integer scopeId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RegisteredClientScopeId id = (RegisteredClientScopeId) o;
            return Objects.equals(registeredClientId, id.registeredClientId) &&
                    Objects.equals(scopeId, id.scopeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registeredClientId, scopeId);
        }
    }

    @EmbeddedId
    private RegisteredClientScopeId id = new RegisteredClientScopeId();

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
            name = "scope_id",
            insertable = false, updatable = false)
    private Scope scope;

    public RegisteredClientScope(Integer assignedBy, JPARegisteredClient registeredClient, Scope scope) {
        this.assignedBy = assignedBy;
        this.registeredClient = registeredClient;
        this.scope = scope;

        this.id.registeredClientId = registeredClient.getId();
        this.id.scopeId = scope.getId();

        registeredClient.addScope(this);
        scope.addRegisteredClientScope(this);
    }
}
