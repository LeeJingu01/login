package com.springboot.jwttest.jwt.auth.controller;

import com.springboot.jwttest.jwt.auth.dto.MeRes;
import com.springboot.jwttest.user.mapper.UserMapper;
import com.springboot.jwttest.user.repository.UserRepository;
import com.springboot.jwttest.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<MeRes> me(Authentication authentication) {
        if(authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) authentication.getPrincipal();
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(MeRes.from(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
