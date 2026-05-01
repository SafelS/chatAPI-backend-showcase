package com.chatAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponseDto {

    private LocalDateTime sentAt;


}
