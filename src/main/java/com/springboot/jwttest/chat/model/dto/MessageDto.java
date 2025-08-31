package com.springboot.jwttest.chat.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    public enum MessageType {
        JOIN, TALK, LEAVE
    }

    @JsonProperty("messageType")
    private MessageType messageType;
    @JsonProperty("chatRoomId")
    private Long chatRoomId;
    @JsonProperty("message")
    private String message;
}
