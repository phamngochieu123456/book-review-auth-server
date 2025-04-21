package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.PermissionDTO;
import com.hieupn.auth.model.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    @Mapping(target = "userPermissions", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    Permission toPermission(PermissionDTO permissionDTO);

    PermissionDTO toPermissionDTO(Permission permission);

    List<PermissionDTO> toPermissionDTOs(List<Permission> permissions);
}