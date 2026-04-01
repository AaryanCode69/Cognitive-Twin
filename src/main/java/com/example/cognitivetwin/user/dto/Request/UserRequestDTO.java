package com.example.cognitivetwin.user.dto.Request;

import com.example.cognitivetwin.common.annotations.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
public record UserRequestDTO(@Email @NotBlank String email, @NotBlank @StrongPassword String password) {
}
