package com.chatAPI.services;


import com.chatAPI.dtos.MessageRequestDto;
import com.chatAPI.dtos.MessageResponseDto;
import com.chatAPI.entities.Chatroom;
import com.chatAPI.entities.Message;
import com.chatAPI.entities.User;
import com.chatAPI.repositories.ChatroomRepository;
import com.chatAPI.repositories.MessageRepository;
import com.chatAPI.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;

    public MessageResponseDto createMessage(MessageRequestDto requestDto){

        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found!"));
        Chatroom chatroom = chatroomRepository.findById(requestDto.getChatroomId()).orElseThrow(()-> new RuntimeException(("Chatroom does not exist!")));

        Message message = new Message();
        message.setText(requestDto.getMessage());
        message.setUser(user);
        message.setChatroom(chatroom);
        message.setSentAt(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        return  new MessageResponseDto(savedMessage.getSentAt());


    }
}
