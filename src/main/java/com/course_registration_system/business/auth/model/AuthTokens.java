package com.course_registration_system.business.auth.model;

// expiresIn is the access token lifetime in seconds
public record AuthTokens(String accessToken, String refreshToken, long expiresIn) {
}
