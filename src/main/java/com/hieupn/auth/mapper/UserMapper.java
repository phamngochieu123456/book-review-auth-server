package com.hieupn.auth.mapper;

import com.hieupn.auth.model.dto.PermissionDTO;
import com.hieupn.auth.model.dto.RoleDTO;
import com.hieupn.auth.model.dto.UserDTO;
import com.hieupn.auth.model.entity.User;
import com.hieupn.auth.model.entity.UserPermission;
import com.hieupn.auth.model.entity.UserRole;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, PermissionMapper.class})
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	UserDTO toUserDTO(User user);

	@Mapping(target = "userRoles", ignore = true)
	@Mapping(target = "userPermissions", ignore = true)
	User toUser(UserDTO userDTO);

	@AfterMapping
	default void mapRoles(User user, @MappingTarget UserDTO userDTO) {
		Set<UserRole> userRoles = user.getUserRoles();
		if (userRoles != null && !userRoles.isEmpty()) {
			List<RoleDTO> roleDTOs = userRoles.stream()
					.map(userRole -> RoleMapper.INSTANCE.toRoleDTO(userRole.getRole()))
					.collect(Collectors.toList());
			userDTO.setRoles(roleDTOs);
		}
	}

	@AfterMapping
	default void mapPermissions(User user, @MappingTarget UserDTO userDTO) {
		Set<UserPermission> userPermissions = user.getUserPermissions();
		if (userPermissions != null && !userPermissions.isEmpty()) {
			List<PermissionDTO> permissionDTOs = userPermissions.stream()
					.map(userPermission -> PermissionMapper.INSTANCE.toPermissionDTO(userPermission.getPermission()))
					.collect(Collectors.toList());
			userDTO.setPermissions(permissionDTOs);
		}
	}

	List<UserDTO> toUserDTOs(List<User> users);
}
