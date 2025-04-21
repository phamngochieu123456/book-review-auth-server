package com.hieupn.auth.controller.advice;

import com.hieupn.auth.exception.DuplicateResourceException;
import com.hieupn.auth.exception.InvalidRequestException;
import com.hieupn.auth.exception.ResourceNotFoundException;
import com.hieupn.auth.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Standard error response structure
    private Map<String, Object> buildErrorResponse(String error, String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return errorResponse;
    }

    // Handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse("not_found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle DuplicateResourceException
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        return buildErrorResponse("conflict", ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Handle InvalidRequestException
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        return buildErrorResponse("bad_request", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle UnauthorizedException
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        return buildErrorResponse("unauthorized", ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Handle Spring Security authentication errors
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse("forbidden", "Access to this resource is denied", HttpStatus.FORBIDDEN);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        // Create a map containing all validation errors by field
        Map<String, String> fieldErrors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        // If there are multiple errors on one field, concatenate them
                        (error1, error2) -> error1 + "; " + error2
                ));

        Map<String, Object> response = buildErrorResponse(
                "validation_error",
                "Invalid input data",
                HttpStatus.BAD_REQUEST
        );

        // Add detailed validation errors to the response
        response.put("fieldErrors", fieldErrors);

        return response;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "unauthorized");
        body.put("message", "Authentication required");

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class) // Specifically handle BadCredentialsException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleBadCredentialsException(Exception ex, WebRequest request) {
        return buildErrorResponse("unauthorized", "Authentication failed", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getReason());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, ex.getStatusCode());
    }

//    // Handle all other unhandled exceptions
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Map<String, Object> handleGlobalException(Exception ex, WebRequest request) {
//        // Log the exception for debugging
//        ex.printStackTrace();
//
//        return buildErrorResponse(
//                "internal_server_error",
//                "An unexpected error occurred. Please try again later.",
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }
}
