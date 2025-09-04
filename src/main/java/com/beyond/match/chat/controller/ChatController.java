package com.beyond.match.chat.controller;

import com.beyond.match.chat.model.dto.ChatRoomListResDto;
import com.beyond.match.chat.model.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/group/create")
    public ResponseEntity<?> createGroupRoom(@RequestParam String roomName) {
        chatService.createGroupRoom(roomName);

        return ResponseEntity.ok().build();
    }

    // 그룹채팅목록조회
    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        List<ChatRoomListResDto> chatRoomListResDtos = chatService.getGroupChatRooms();
        return new ResponseEntity<>(chatRoomListResDtos, HttpStatus.OK);
    }

    // 그룹채팅방참여
    @PostMapping("/group/{roomId}")
    public ResponseEntity<?> joinGroupChatRoom(@PathVariable int roomId) {
        chatService.addParticipantToGroupChat(roomId);
        return ResponseEntity.ok().build();
    }
}
