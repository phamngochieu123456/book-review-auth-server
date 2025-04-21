package com.hieupn.auth.security;

import com.hieupn.auth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Customizes the JWT token by adding user roles, permissions, and user_id as claims.
 * This enhances the token with additional user authorization information
 * that can be used by resource servers to make authorization decisions.
 */
@Component
public class TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final UserRepository userRepository;

    public TokenCustomizer(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void customize(JwtEncodingContext context) {
        // Only customize access tokens
        if (context.getTokenType() == OAuth2TokenType.ACCESS_TOKEN) {
            Authentication principal = context.getPrincipal();

            // Extract user_id from the principal
            if (principal.getPrincipal() instanceof User user) {
                Optional<com.hieupn.auth.model.entity.User> optionalJpaUser =
                        userRepository.findByUsername(user.getUsername());
                if (!optionalJpaUser.isEmpty()) {
                    com.hieupn.auth.model.entity.User jpaUser =
                            optionalJpaUser.get();
                    context.getClaims().claim("user_id", jpaUser.getId());
                }
            }

            // Extract roles (authorities that start with ROLE_)
            Set<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> authority.startsWith("ROLE_"))
                    // Remove ROLE_ prefix for cleaner token claims
                    .map(authority -> authority.substring(5))
                    .collect(Collectors.toSet());

            // Extract permissions (authorities that don't start with ROLE_)
            Set<String> permissions = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> !authority.startsWith("ROLE_"))
                    .collect(Collectors.toSet());

            // Add roles and permissions to the token claims
            context.getClaims().claim("roles", roles);
            context.getClaims().claim("permissions", permissions);
        }
    }
}
