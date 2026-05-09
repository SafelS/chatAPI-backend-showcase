package com.chatAPI.repositories;

import com.chatAPI.entities.Chatroom;
import com.chatAPI.enums.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    @Query("""
    SELECT c FROM ChatRoom c
    JOIN c.memberships m1
    JOIN c.memberships m2
    WHERE m1.user.id = :userAId
    AND m2.user.id = :userBId
    AND c.type = :type
    """)
    Optional<Chatroom> findPrivateChatBetweenUsers(
            @Param("userAId") Long userAId,
            @Param("userBId") Long userBId,
            @Param("type") ChatType type
    );

    Optional<Chatroom> findByName(String name);
}
