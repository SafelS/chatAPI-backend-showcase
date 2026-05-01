package com.chatAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequestDto {

    private String message;
    private Long userId;
    private Long chatroomId;

}
