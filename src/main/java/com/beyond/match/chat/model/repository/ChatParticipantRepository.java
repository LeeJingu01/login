package com.beyond.match.chat.model.repository;

import com.beyond.match.chat.model.vo.ChatRoom;
import com.beyond.match.chat.model.vo.JoinedChatRoom;
import com.beyond.match.user.model.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<JoinedChatRoom, Integer> {
    List<JoinedChatRoom> findByChatRoom(ChatRoom chatRoom);
    Optional<JoinedChatRoom> findByChatRoomAndUser(ChatRoom chatRoom, User user);
}
