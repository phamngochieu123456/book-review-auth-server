package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.PermissionDTO;
import com.hieupn.auth.model.dto.RoleDTO;
import com.hieupn.auth.model.entity.Role;
import com.hieupn.auth.model.entity.RolePermission;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    Role toRole(RoleDTO roleDTO);

    @Mapping(target = "permissions", ignore = true)
    RoleDTO toRoleDTO(Role role);

    @AfterMapping
    default void mapPermissions(Role role, @MappingTarget RoleDTO roleDTO) {
        Set<RolePermission> rolePermissions = role.getRolePermissions();
        if (rolePermissions != null && !rolePermissions.isEmpty()) {
            List<PermissionDTO> permissionDTOs = rolePermissions.stream()
                    .map(rolePermission -> PermissionMapper.INSTANCE.toPermissionDTO(rolePermission.getPermission()))
                    .collect(Collectors.toList());
            roleDTO.setPermissions(permissionDTOs);
        }
    }

    List<RoleDTO> toRoleDTOs(List<Role> roles);
}
