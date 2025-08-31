package com.springboot.jwttest.chat.controller;

import com.springboot.jwttest.chat.model.dto.ChatDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompController {

    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/sub/chat/{chatRoomId}")
    public String message(
            @DestinationVariable Long chatRoomId,
            @Payload ChatDto request
            ){
        log.info("chatRoomId:{}, message: {}",chatRoomId, request.getMessage());

        return request.getMessage();
    }
}


