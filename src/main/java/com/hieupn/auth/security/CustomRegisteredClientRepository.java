package com.hieupn.auth.security;

import com.hieupn.auth.model.entity.*;
import com.hieupn.auth.repository.JPARegisteredClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final JPARegisteredClientRepository jpaRegisteredClientRepository;

    public CustomRegisteredClientRepository(JPARegisteredClientRepository jpaRegisteredClientRepository) {
        this.jpaRegisteredClientRepository = jpaRegisteredClientRepository;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        // Implementation of save is beyond the scope of this example
        // In a real application, we would convert the RegisteredClient to our entity model
        // and save it to the database
        throw new UnsupportedOperationException("Client registration through this repository is not supported");
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findById(String id) {
        return jpaRegisteredClientRepository.findById(id)
                .map(this::toSpringRegisteredClient)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findByClientId(String clientId) {
        // Find client by client ID
        return jpaRegisteredClientRepository.findByClientId(clientId)
                .map(this::toSpringRegisteredClient)
                .orElse(null);
    }

    private RegisteredClient toSpringRegisteredClient(JPARegisteredClient client) {
        // Start building the Spring RegisteredClient
        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientName(client.getClientName());
//                .clientIdIssuedAt(client.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());

        // Set client authentication methods
        for (RegisteredClientAuthenticationMethod authMethod : client.getAuthenticationMethods()) {
            String methodName = authMethod.getAuthenticationMethod().getName();

            // Map common authentication methods
            switch (methodName) {
                case "client_secret_basic":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    break;
                case "client_secret_post":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                    break;
                case "private_key_jwt":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT);
                    break;
                case "none":
                    builder.clientAuthenticationMethod(ClientAuthenticationMethod.NONE);
                    break;
                default:
                    // Custom authentication method
                    builder.clientAuthenticationMethod(new ClientAuthenticationMethod(methodName));
            }
        }

        // Set authorization grant types
        for (RegisteredClientGrantType grantType : client.getGrantTypes()) {
            String grantTypeName = grantType.getGrantType().getName();

            // Map common grant types
            switch (grantTypeName) {
                case "authorization_code":
                    builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
                    break;
                case "client_credentials":
                    builder.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    break;
                case "refresh_token":
                    builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);
                    break;
                case "password":
                    builder.authorizationGrantType(new AuthorizationGrantType("password"));
                    break;
                case "implicit":
                    builder.authorizationGrantType(new AuthorizationGrantType("implicit"));
                    break;
                default:
                    // Custom grant type
                    builder.authorizationGrantType(new AuthorizationGrantType(grantTypeName));
            }
        }

        // Set redirect URIs
        for (RegisteredClientRedirectUri redirectUri : client.getRedirectUris()) {
            builder.redirectUri(redirectUri.getRedirectUri().getUri());
        }

        // Set scopes
        for (RegisteredClientScope scope : client.getScopes()) {
            builder.scope(scope.getScope().getName());
        }

        // Set default token settings (these can be customized based on your requirements)
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(300))
                .refreshTokenTimeToLive(Duration.ofMinutes(30))
                .build();

        // Set default client settings
        ClientSettings clientSettings = ClientSettings.builder()
                .requireAuthorizationConsent(true)
                .build();

        return builder
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();
    }
}
