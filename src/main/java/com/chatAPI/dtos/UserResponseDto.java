package com.chatAPI.dtos;

import com.chatAPI.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {

    private String username;
    private String email;
    private Status status;
}
