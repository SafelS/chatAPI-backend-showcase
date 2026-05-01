package com.chatAPI.repositories;

import com.chatAPI.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
