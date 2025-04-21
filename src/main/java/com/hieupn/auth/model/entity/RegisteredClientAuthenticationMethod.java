package com.hieupn.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "registered_client_authentication_method")
@Data
@NoArgsConstructor
@org.hibernate.annotations.Immutable
public class RegisteredClientAuthenticationMethod {

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisteredClientAuthenticationMethodId implements Serializable {
        @Column(name = "registered_client_id", length = 100)
        private String registeredClientId;

        @Column(name = "authentication_method_id")
        private Integer authenticationMethodId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RegisteredClientAuthenticationMethodId id = (RegisteredClientAuthenticationMethodId) o;
            return Objects.equals(registeredClientId, id.registeredClientId) &&
                    Objects.equals(authenticationMethodId, id.authenticationMethodId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registeredClientId, authenticationMethodId);
        }
    }

    @EmbeddedId
    private RegisteredClientAuthenticationMethodId id = new RegisteredClientAuthenticationMethodId();

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
            name = "authentication_method_id",
            insertable = false, updatable = false)
    private ClientAuthenticationMethod authenticationMethod;

    public RegisteredClientAuthenticationMethod(Integer assignedBy, JPARegisteredClient registeredClient, ClientAuthenticationMethod authenticationMethod) {
        this.assignedBy = assignedBy;
        this.registeredClient = registeredClient;
        this.authenticationMethod = authenticationMethod;

        this.id.registeredClientId = registeredClient.getId();
        this.id.authenticationMethodId = authenticationMethod.getId();

        registeredClient.addAuthenticationMethod(this);
        authenticationMethod.addRegisteredClientAuthenticationMethod(this);
    }
}
