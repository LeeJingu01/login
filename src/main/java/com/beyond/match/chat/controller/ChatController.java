package com.beyond.match.chat.controller;

import com.beyond.match.chat.model.dto.ChatDto;
import com.beyond.match.chat.model.dto.ChatRoomListResDto;
import com.beyond.match.chat.model.dto.MyChatListResDto;
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

    // 채팅방 들어갈때 db에 쌓여있는 메세지들 불러오기
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable int roomId) {
        List<ChatDto> chatDtos = chatService.getChatHistory(roomId);
        return new ResponseEntity<>(chatDtos, HttpStatus.OK);
    }

    // 채팅메시지 읽음처리
    @PostMapping("/{roodId}/read")
    public ResponseEntity<?> messageRead(@PathVariable int roomId) {
        chatService.messageRead(roomId);
        return ResponseEntity.ok().build();
    }

    // 내 채팅방 목록 조회 : roomId, roomName, 그룹채팅여부, 메시지 읽음 개수
    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyRooms() {
        List<MyChatListResDto> myChatListResDtos = chatService.getMyChatRooms();
        return new ResponseEntity<>();
    }

    // 채팅방 나가기
    @DeleteMapping("/group/{roodId}/leave")
    public ResponseEntity<?> leaveGroup(@PathVariable int roomId) {
        chatService.leaveGroupChatRoom(roomId);
        return ResponseEntity.ok().build();
    }

}
