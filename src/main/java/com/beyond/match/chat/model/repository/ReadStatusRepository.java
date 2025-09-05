package com.beyond.match.chat.model.repository;

import com.beyond.match.chat.model.vo.ChatRoom;
import com.beyond.match.chat.model.vo.MessageReadStatus;
import com.beyond.match.user.model.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadStatusRepository extends JpaRepository<MessageReadStatus, Integer> {
    List<MessageReadStatus> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    int countByChatRoomAndUserAndIsReadFalse(ChatRoom chatRoom, User user);
}
