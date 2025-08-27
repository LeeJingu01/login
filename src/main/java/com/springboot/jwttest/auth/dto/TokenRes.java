package com.springboot.jwttest.auth.dto;

public record TokenRes(
        String accessToken,
        String refreshToken,
        long   expiresInSec
) {}
