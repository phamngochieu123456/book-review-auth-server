package com.hieupn.auth.security;

import com.hieupn.auth.model.entity.RolePermission;
import com.hieupn.auth.model.entity.User;
import com.hieupn.auth.model.entity.UserPermission;
import com.hieupn.auth.model.entity.UserRole;
import com.hieupn.auth.model.enums.UserStatusType;
import com.hieupn.auth.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        User user = userOptional.get();

        // Collect authorities (roles and permissions)
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add roles as authorities with ROLE_ prefix (Spring Security convention)
        for (UserRole userRole : user.getUserRoles()) {
            String roleName = "ROLE_" + userRole.getRole().getName().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(roleName));

            // Add permissions from each role
            for (RolePermission rolePermission : userRole.getRole().getRolePermissions()) {
                String permissionName = rolePermission.getPermission().getName().toUpperCase();
                authorities.add(new SimpleGrantedAuthority(permissionName));
            }
        }

        // Add direct user permissions
        for (UserPermission userPermission : user.getUserPermissions()) {
            String permissionName = userPermission.getPermission().getName().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(permissionName));
        }

        // Create Spring Security User object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // Password is already BCrypt encoded in the DB
                .authorities(authorities)
                .accountExpired(UserStatusType.INACTIVE.equals(user.getStatus()))
                .accountLocked(UserStatusType.INACTIVE.equals(user.getStatus()))
                .credentialsExpired(false)
                .disabled(UserStatusType.INACTIVE.equals(user.getStatus()))
                .build();
    }
}
