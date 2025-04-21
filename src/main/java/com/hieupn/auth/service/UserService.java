package com.hieupn.auth.service;

import com.hieupn.auth.exception.DuplicateResourceException;
import com.hieupn.auth.exception.InvalidRequestException;
import com.hieupn.auth.exception.ResourceNotFoundException;
import com.hieupn.auth.model.dto.PasswordChangeDTO;
import com.hieupn.auth.model.dto.UserProfileDTO;
import com.hieupn.auth.model.dto.UserRegistrationDTO;
import com.hieupn.auth.model.entity.Role;
import com.hieupn.auth.model.entity.User;
import com.hieupn.auth.model.entity.UserRole;
import com.hieupn.auth.model.enums.UserStatusType;
import com.hieupn.auth.repository.RoleRepository;
import com.hieupn.auth.repository.UserRepository;
import com.hieupn.auth.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Check if username already exists
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new DuplicateResourceException("User", "username", registrationDTO.getUsername());
        }

        // Check if email already exists
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", registrationDTO.getEmail());
        }

        // Check if password and confirmation match
        if (!registrationDTO.getPassword().equals(registrationDTO.getPasswordConfirmation())) {
            throw new InvalidRequestException("Password and confirmation do not match");
        }

        // Create new user
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setStatus(UserStatusType.ACTIVE);

        // Save user
        User savedUser = userRepository.save(user);

        // Assign default USER role
        Optional<Role> userRole = roleRepository.findByName("USER");
        if (userRole.isEmpty()) {
            throw new ResourceNotFoundException("Role", "name", "USER");
        }

        // Admin user (ID=1) assigns this role
        UserRole userRoleEntity = new UserRole(1, savedUser, userRole.get());
        userRoleRepository.save(userRoleEntity);

        return savedUser;
    }

    public UserProfileDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setId(user.getId());
        profileDTO.setUsername(user.getUsername());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setStatus(user.getStatus().toString());

        return profileDTO;
    }

    @Transactional
    public UserProfileDTO updateUserProfile(String username, UserProfileDTO profileDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Check if email is being changed and not already used by another user
        if (!user.getEmail().equals(profileDTO.getEmail())) {
            userRepository.findByEmail(profileDTO.getEmail())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(user.getId())) {
                            throw new DuplicateResourceException("User", "email", profileDTO.getEmail());
                        }
                    });
            user.setEmail(profileDTO.getEmail());
        }

        // Save updated user
        User updatedUser = userRepository.save(user);

        // Convert to DTO
        UserProfileDTO updatedProfileDTO = new UserProfileDTO();
        updatedProfileDTO.setId(updatedUser.getId());
        updatedProfileDTO.setUsername(updatedUser.getUsername());
        updatedProfileDTO.setEmail(updatedUser.getEmail());
        updatedProfileDTO.setStatus(updatedUser.getStatus().toString());

        return updatedProfileDTO;
    }

    @Transactional
    public void changePassword(String username, PasswordChangeDTO passwordChangeDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Verify current password
        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new InvalidRequestException("Current password is incorrect");
        }

        // Check if new password and confirmation match
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getPasswordConfirmation())) {
            throw new InvalidRequestException("New password and confirmation do not match");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
    }
}
