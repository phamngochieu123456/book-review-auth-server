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
public class RedirectUriDTO {

    private Integer id;

    @NotBlank(message = "Redirect URI cannot be blank")
    @Size(max = 200, message = "Redirect URI cannot exceed 200 characters")
    private String uri;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
