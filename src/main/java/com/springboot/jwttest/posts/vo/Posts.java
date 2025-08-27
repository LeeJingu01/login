package com.springboot.jwttest.posts.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Posts {
    private int postsId;
    private int userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
