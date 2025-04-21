package com.hieupn.auth.model.dto;

import com.hieupn.auth.model.enums.UserStatusType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Integer id;

	@NotBlank(message = "Username cannot be blank")
	@Size(max = 50, message = "Username cannot exceed 50 characters")
	private String username;

	@NotBlank(message = "Password cannot be blank")
	private String password;

	@NotBlank(message = "Email cannot be blank")
	@Size(max = 100, message = "Email cannot exceed 100 characters")
	@Email(message = "Email should be valid")
	private String email;

	@NotNull(message = "Status cannot be null")
	private UserStatusType status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private List<RoleDTO> roles;

	private List<PermissionDTO> permissions;
}
