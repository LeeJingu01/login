package com.beyond.match.chat.model.service;

import com.beyond.match.chat.model.dto.ChatDto;
import com.beyond.match.chat.model.dto.ChatRoomListResDto;
import com.beyond.match.chat.model.dto.MyChatListResDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    public void saveMessage(int chatRoomId, ChatDto message) {
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
        String nickname = userDetails.getUser().getNickname();
        User user = userRepository.findByNickname(nickname);
        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(roomName)
                .isGroupChat("Y")
                .build();
        chatRoomRepository.save(chatRoom);
        // 채팅 참여자로 개설자 추가
        JoinedChatRoom joinedChatRoom = JoinedChatRoom.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatParticipantRepository.save(joinedChatRoom);
    }

    public List<ChatRoomListResDto> getGroupChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
        List<ChatRoomListResDto> dtos = new ArrayList<>();
        for (ChatRoom c : chatRooms) {
            ChatRoomListResDto dto = ChatRoomListResDto.builder()
                    .roomId(c.getChatRoomId())
                    .roomName(c.getChatRoomName())
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }

    public void addParticipantToGroupChat(int roodId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roodId).orElseThrow(()-> new  EntityNotFoundException("Chat Room Not Found"));
        // 유저 조회
        User user = userRepository.findByNickname(userDetails.getUser().getNickname());
        // 이미 참여자인지 검증
        Optional<JoinedChatRoom> participant = chatParticipantRepository.findByChatRoomAndUser(chatRoom, user);
        if(!participant.isPresent()){
            addParticipantToRoom(chatRoom, user);
        }


    }
    // join 객체 생성 후 저장
    public void addParticipantToRoom(ChatRoom chatRoom, User user) {
        JoinedChatRoom chatParticipant = JoinedChatRoom.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatDto> getChatHistory(int roomId) {
        // 내가 해당 채팅방의 참여자가 아닐 경우 에러 발생
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("Chat Room Not Found"));
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByNickname(userDetails.getUser().getNickname());
        List<JoinedChatRoom> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        boolean check = false;
        for (JoinedChatRoom c : chatParticipants){
            if(c.getUser().equals(user)){
                check = true;
            }
        }
        if(!check){
            throw new IllegalStateException("본인이 속하지 않은 채팅방입니다.");
        }
        // 특정 룸에 대한 message 조회
        List<Message> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom);
        List<ChatDto> dtos = new ArrayList<>();
        for (Message m : chatMessages) {
            ChatDto chatDto = ChatDto.builder()
                    .message(m.getContent())
                    .senderNickname(m.getUser().getNickname())
                    .build();
            dtos.add(chatDto);
        }
        return dtos;
    }

    public boolean isRoomParticipant(String nickname, int roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("Chat Room Not Found"));

        User user = userRepository.findByNickname(nickname);

        List<JoinedChatRoom> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for (JoinedChatRoom c : chatParticipants){
            if(c.getUser().getUserId()==(user.getUserId())){
                return true;
                }
            }
        return false;
        }

    public void messageRead(int roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("Chat Room Not Found"));
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByNickname(userDetails.getUser().getNickname());
        List<MessageReadStatus> statuses = readStatusRepository.findByChatRoomAndUser(chatRoom, user);
        for(MessageReadStatus m : statuses){
            m.updateIsRead(true);
        }
    }

    public List<MyChatListResDto> getMyChatRooms() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByNickname(userDetails.getUser().getNickname());
        List<JoinedChatRoom> chatParticipants = chatParticipantRepository.findAllByUser(user);
        List<MyChatListResDto> dtos = new ArrayList<>();
        for (JoinedChatRoom c : chatParticipants){
            int count = readStatusRepository.countByChatRoomAndUserAndIsReadFalse(c.getChatRoom(), user);
            MyChatListResDto dto = MyChatListResDto.builder()
                    .roomId(c.getChatRoom().getChatRoomId())
                    .roomName(c.getChatRoom().getChatRoomName())
                    .isGroupChat(c.getChatRoom().getIsGroupChat())
                    .unReadCount(count)
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }

    public void leaveGroupChatRoom(int roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("Chat Room Not Found"));
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByNickname(userDetails.getUser().getNickname());
        if(chatRoom.getIsGroupChat().equals("N")){
            throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
        }
        JoinedChatRoom joinUser = chatParticipantRepository.findByChatRoomAndUser(chatRoom, user).orElseThrow(()->
                new EntityNotFoundException("참여자를 찾을 수 없습니다."));
        chatParticipantRepository.delete(joinUser);
        chatRoomRepository.delete(chatRoom);
    }
}
