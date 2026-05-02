package com.chatAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponseDto {

    private Long id;
    private String message;
    private String senderName;
    private Long chatroomId;
    private String chatroomName;
    private LocalDateTime sentAt;



}
