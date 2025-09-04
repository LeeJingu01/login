package com.beyond.match.chat.model.repository;

import com.beyond.match.chat.model.vo.ChatRoom;
import com.beyond.match.chat.model.vo.JoinedChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<JoinedChatRoom, Integer> {
    List<JoinedChatRoom> findByChatRoom(ChatRoom chatRoom);
}
