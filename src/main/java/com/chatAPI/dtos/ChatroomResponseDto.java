package com.chatAPI.dtos;


import com.chatAPI.entities.ChatRoomMembership;
import com.chatAPI.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatroomResponseDto {

    private Long id;
    private String name;
    private ChatType type;


}
