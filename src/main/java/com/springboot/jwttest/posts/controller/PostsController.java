package com.springboot.jwttest.posts.controller;

import com.springboot.jwttest.posts.dto.PostsRequestDto;
import com.springboot.jwttest.posts.service.PostsService;
import com.springboot.jwttest.posts.vo.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;

    @PostMapping("/posts")
    public ResponseEntity<PostsRequestDto> posts(@RequestBody PostsRequestDto postPostsRequestDto){

    }

}
