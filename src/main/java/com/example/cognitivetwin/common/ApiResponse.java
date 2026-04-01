package com.example.cognitivetwin.common;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ApiResponse<T>(T data, Instant timestamp, String path, int status) {
}
