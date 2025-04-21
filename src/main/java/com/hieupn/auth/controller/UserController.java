package com.hieupn.auth.controller;

import com.hieupn.auth.model.dto.PasswordChangeDTO;
import com.hieupn.auth.model.dto.UserProfileDTO;
import com.hieupn.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		UserProfileDTO userProfile = userService.getUserProfile(currentUsername);
		return ResponseEntity.ok(userProfile);
	}

	@PutMapping("/me")
	public ResponseEntity<?> updateCurrentUserProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		UserProfileDTO updatedProfile = userService.updateUserProfile(currentUsername, profileDTO);
		return ResponseEntity.ok(updatedProfile);
	}

	@PostMapping("/me/change-password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		userService.changePassword(currentUsername, passwordChangeDTO);

		Map<String, String> response = new HashMap<>();
		response.put("message", "Password changed successfully");
		return ResponseEntity.ok(response);
	}
}
