package com.beyond.match.chat.model.service;

import com.beyond.match.chat.model.dto.ChatDto;
import com.beyond.match.chat.model.repository.ChatMessageRepository;
import com.beyond.match.chat.model.repository.ChatParticipantRepository;
import com.beyond.match.chat.model.repository.ChatRoomRepository;
import com.beyond.match.chat.model.repository.ReadStatusRepository;
import com.beyond.match.chat.model.vo.ChatRoom;
import com.beyond.match.chat.model.vo.JoinedChatRoom;
import com.beyond.match.chat.model.vo.Message;
import com.beyond.match.chat.model.vo.MessageReadStatus;
import com.beyond.match.jwt.auth.model.UserDetailsImpl;
import com.beyond.match.user.model.repository.UserRepository;
import com.beyond.match.user.model.vo.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    public void saMessage(int chatRoomId, ChatDto message) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()->
                new EntityNotFoundException("Chat Room Not Found"));
        // 보낸 사람 조회
        User sender = userRepository.findByNickname(message.getSenderNickname());
        if(sender == null){
            throw new EntityNotFoundException("User Not Found");
        }
        // 메세지 저장
        Message ms = Message.builder()
                .chatRoom(chatRoom)
                .user(sender)
                .content(message.getMessage())
                .build();
        chatMessageRepository.save(ms);
        // 사용자별로 읽음여부 저장
        List<JoinedChatRoom>  joinedChatRooms = chatParticipantRepository.findByChatRoom(chatRoom);
        for (JoinedChatRoom joinedChatRoom : joinedChatRooms) {
            MessageReadStatus messageReadStatus = MessageReadStatus.builder()
                    .chatRoom(chatRoom)
                    .user(joinedChatRoom.getUser())
                    .message(ms)
                    .isRead(joinedChatRoom.getUser().equals(sender))
                    .build();
            readStatusRepository.save(messageReadStatus);
        }
    }

    public void createGroupRoom(String roomName) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if(userDetails==null){
            return;
        }
        String nickname = userDetails.getUser().getNickname();
        User user = userRepository.findByNickname(nickname);
    }
}
