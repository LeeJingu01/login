package com.springboot.jwttest.posts.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsRequestDto {
    private String title;
    private String content;
}
