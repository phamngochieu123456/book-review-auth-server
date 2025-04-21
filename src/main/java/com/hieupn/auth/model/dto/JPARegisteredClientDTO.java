package com.hieupn.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JPARegisteredClientDTO {

    @NotBlank(message = "Client ID cannot be blank")
    @Size(max = 100, message = "Client ID cannot exceed 100 characters")
    private String id;

    @NotBlank(message = "Client identifier cannot be blank")
    @Size(max = 100, message = "Client identifier cannot exceed 100 characters")
    private String clientId;

    @NotBlank(message = "Client secret cannot be blank")
    private String clientSecret;

    @NotBlank(message = "Client name cannot be blank")
    private String clientName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ClientAuthenticationMethodDTO> authenticationMethods;

    private List<AuthorizationGrantTypeDTO> grantTypes;

    private List<RedirectUriDTO> redirectUris;

    private List<ScopeDTO> scopes;
}
