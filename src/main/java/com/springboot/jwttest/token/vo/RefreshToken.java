package com.springboot.jwttest.token.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefreshToken {
    private Integer userId;
    private String tokenHash;
    private LocalDateTime expiresAt;
}
