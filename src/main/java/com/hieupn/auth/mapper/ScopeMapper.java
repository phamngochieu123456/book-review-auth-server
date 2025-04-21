package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.ScopeDTO;
import com.hieupn.auth.model.entity.Scope;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScopeMapper {

    ScopeMapper INSTANCE = Mappers.getMapper(ScopeMapper.class);

    @Mapping(target = "registeredClientScopes", ignore = true)
    Scope toScope(ScopeDTO scopeDTO);

    ScopeDTO toScopeDTO(Scope scope);

    List<ScopeDTO> toScopeDTOs(List<Scope> scopes);
}
