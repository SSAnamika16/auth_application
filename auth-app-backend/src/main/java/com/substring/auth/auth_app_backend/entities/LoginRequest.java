package com.substring.auth.auth_app_backend.entities;

public record LoginRequest(
        String email,
        String password
) {
}
