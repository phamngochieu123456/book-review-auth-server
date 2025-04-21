package com.hieupn.auth.aop;

import com.hieupn.auth.exception.UnauthorizedException;
import com.hieupn.auth.model.dto.UserProfileDTO;
import com.hieupn.auth.model.entity.User;
import com.hieupn.auth.repository.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class UserSecurityAspect {

    private final UserRepository userRepository;

    public UserSecurityAspect(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Define pointcuts for user-related operations
    @Pointcut("execution(* com.hieupn.auth.controller.UserController.getCurrentUserProfile(..))")
    public void getUserProfileOperation() {}

    @Pointcut("execution(* com.hieupn.auth.controller.UserController.updateCurrentUserProfile(..))")
    public void updateUserProfileOperation() {}

    @Pointcut("execution(* com.hieupn.auth.controller.UserController.changePassword(..))")
    public void changePasswordOperation() {}

    // Apply security check around user information operations
    @Around("updateUserProfileOperation()")
    public Object enforceUserProfileUpdateSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuth.getToken();

            // Extract user_id from JWT token safely using Number.intValue()
            Number userIdNumber = jwt.getClaim("user_id");
            if (userIdNumber == null) {
                throw new UnauthorizedException("User ID not found in token");
            }

            Integer userIdFromToken = userIdNumber.intValue();

            // Fetch the user from database by ID
            Optional<User> optionalUser = userRepository.findById(userIdFromToken);

            if (optionalUser.isEmpty()) {
                throw new UnauthorizedException("User not found with ID from token");
            }

            User userFromDb = optionalUser.get();

            // Get the UserProfileDTO from the method arguments
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof UserProfileDTO) {
                    UserProfileDTO profileDTO = (UserProfileDTO) arg;
                    String usernameFromDTO = profileDTO.getUsername();

                    // If DTO contains username and it doesn't match the user from token
                    if (usernameFromDTO != null && !usernameFromDTO.equals(userFromDb.getUsername())) {
                        throw new UnauthorizedException("Cannot update profile: username mismatch");
                    }

                    break;
                }
            }
        }

        // If validation passes or not applicable, proceed with the original method
        return joinPoint.proceed();
    }
}
