package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.AuthorizationGrantTypeDTO;
import com.hieupn.auth.model.dto.ClientAuthenticationMethodDTO;
import com.hieupn.auth.model.dto.RedirectUriDTO;
import com.hieupn.auth.model.dto.JPARegisteredClientDTO;
import com.hieupn.auth.model.dto.ScopeDTO;
import com.hieupn.auth.model.entity.JPARegisteredClient;
import com.hieupn.auth.model.entity.RegisteredClientAuthenticationMethod;
import com.hieupn.auth.model.entity.RegisteredClientGrantType;
import com.hieupn.auth.model.entity.RegisteredClientRedirectUri;
import com.hieupn.auth.model.entity.RegisteredClientScope;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        ClientAuthenticationMethodMapper.class,
        AuthorizationGrantTypeMapper.class,
        RedirectUriMapper.class,
        ScopeMapper.class
})
public interface JPARegisteredClientMapper {

    JPARegisteredClientMapper INSTANCE = Mappers.getMapper(JPARegisteredClientMapper.class);

    @Mapping(target = "authenticationMethods", ignore = true)
    @Mapping(target = "grantTypes", ignore = true)
    @Mapping(target = "redirectUris", ignore = true)
    @Mapping(target = "scopes", ignore = true)
    JPARegisteredClient toRegisteredClient(JPARegisteredClientDTO registeredClientDTO);

    @Mapping(target = "authenticationMethods", ignore = true)
    @Mapping(target = "grantTypes", ignore = true)
    @Mapping(target = "redirectUris", ignore = true)
    @Mapping(target = "scopes", ignore = true)
    JPARegisteredClientDTO toRegisteredClientDTO(JPARegisteredClient registeredClient);

    @AfterMapping
    default void mapAuthenticationMethods(JPARegisteredClient client, @MappingTarget JPARegisteredClientDTO clientDTO) {
        Set<RegisteredClientAuthenticationMethod> methods = client.getAuthenticationMethods();
        if (methods != null && !methods.isEmpty()) {
            List<ClientAuthenticationMethodDTO> methodDTOs = methods.stream()
                    .map(method -> ClientAuthenticationMethodMapper.INSTANCE.toClientAuthenticationMethodDTO(method.getAuthenticationMethod()))
                    .collect(Collectors.toList());
            clientDTO.setAuthenticationMethods(methodDTOs);
        }
    }

    @AfterMapping
    default void mapGrantTypes(JPARegisteredClient client, @MappingTarget JPARegisteredClientDTO clientDTO) {
        Set<RegisteredClientGrantType> grantTypes = client.getGrantTypes();
        if (grantTypes != null && !grantTypes.isEmpty()) {
            List<AuthorizationGrantTypeDTO> grantTypeDTOs = grantTypes.stream()
                    .map(grantType -> AuthorizationGrantTypeMapper.INSTANCE.toAuthorizationGrantTypeDTO(grantType.getGrantType()))
                    .collect(Collectors.toList());
            clientDTO.setGrantTypes(grantTypeDTOs);
        }
    }

    @AfterMapping
    default void mapRedirectUris(JPARegisteredClient client, @MappingTarget JPARegisteredClientDTO clientDTO) {
        Set<RegisteredClientRedirectUri> redirectUris = client.getRedirectUris();
        if (redirectUris != null && !redirectUris.isEmpty()) {
            List<RedirectUriDTO> redirectUriDTOs = redirectUris.stream()
                    .map(redirectUri -> RedirectUriMapper.INSTANCE.toRedirectUriDTO(redirectUri.getRedirectUri()))
                    .collect(Collectors.toList());
            clientDTO.setRedirectUris(redirectUriDTOs);
        }
    }

    @AfterMapping
    default void mapScopes(JPARegisteredClient client, @MappingTarget JPARegisteredClientDTO clientDTO) {
        Set<RegisteredClientScope> scopes = client.getScopes();
        if (scopes != null && !scopes.isEmpty()) {
            List<ScopeDTO> scopeDTOs = scopes.stream()
                    .map(scope -> ScopeMapper.INSTANCE.toScopeDTO(scope.getScope()))
                    .collect(Collectors.toList());
            clientDTO.setScopes(scopeDTOs);
        }
    }

    List<JPARegisteredClientDTO> toRegisteredClientDTOs(List<JPARegisteredClient> registeredClients);
}
