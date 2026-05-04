package com.chatAPI.repositories;

import com.chatAPI.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatroomId(Long chatroomId, Pageable pageable);
}
