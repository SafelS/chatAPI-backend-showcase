package com.chatAPI.repositories;

import com.chatAPI.entities.ChatRoomMembership;
import com.chatAPI.entities.Chatroom;
import com.chatAPI.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomMembershipRepository extends JpaRepository<ChatRoomMembership, Long> {
    boolean existsByUserAndChatroom(User user, Chatroom chatroom);
    Optional<ChatRoomMembership> findByUserAndChatroom(User user, Chatroom chatroom);
    List<ChatRoomMembership> findByUser(User user);
}
