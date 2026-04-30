package com.chatAPI.entities;


import com.chatAPI.enums.ChatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="chatroom")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.REMOVE)
    private List<ChatRoomMembership> memberships;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.REMOVE)
    private List<Message> messages;



}
