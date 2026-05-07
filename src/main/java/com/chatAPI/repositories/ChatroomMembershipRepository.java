package com.chatAPI.repositories;

import com.chatAPI.entities.ChatRoomMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomMembershipRepository extends JpaRepository<ChatRoomMembership, Long> {
}
