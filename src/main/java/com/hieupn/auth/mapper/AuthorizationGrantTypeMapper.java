package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.AuthorizationGrantTypeDTO;
import com.hieupn.auth.model.entity.AuthorizationGrantType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorizationGrantTypeMapper {

    AuthorizationGrantTypeMapper INSTANCE = Mappers.getMapper(AuthorizationGrantTypeMapper.class);

    @Mapping(target = "registeredClientGrantTypes", ignore = true)
    AuthorizationGrantType toAuthorizationGrantType(AuthorizationGrantTypeDTO authorizationGrantTypeDTO);

    AuthorizationGrantTypeDTO toAuthorizationGrantTypeDTO(AuthorizationGrantType authorizationGrantType);

    List<AuthorizationGrantTypeDTO> toAuthorizationGrantTypeDTOs(List<AuthorizationGrantType> authorizationGrantTypes);
}
