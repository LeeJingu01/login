package com.beyond.match.chat.model.repository;

import com.beyond.match.chat.model.vo.MessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends JpaRepository<MessageReadStatus, Integer> {
}
