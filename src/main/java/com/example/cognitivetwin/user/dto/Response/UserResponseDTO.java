package com.example.cognitivetwin.user.dto.Response;

import com.example.cognitivetwin.user.Role;
import lombok.Builder;

@Builder
public record UserResponseDTO(String email, Role userRole) {
}
