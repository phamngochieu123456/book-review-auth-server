package com.hieupn.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationGrantTypeDTO {

    private Integer id;

    @NotBlank(message = "Grant type name cannot be blank")
    @Size(max = 50, message = "Grant type name cannot exceed 50 characters")
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
