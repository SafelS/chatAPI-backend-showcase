package com.chatAPI.services;


import com.chatAPI.dtos.MessageRequestDto;
import com.chatAPI.dtos.MessageResponseDto;
import com.chatAPI.entities.ChatRoomMembership;
import com.chatAPI.entities.Chatroom;
import com.chatAPI.entities.Message;
import com.chatAPI.entities.User;
import com.chatAPI.enums.ChatType;
import com.chatAPI.repositories.ChatroomRepository;
import com.chatAPI.repositories.MessageRepository;
import com.chatAPI.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageResponseDto createMessage(MessageRequestDto requestDto){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
        Chatroom chatroom = chatroomRepository.findById(requestDto.getChatroomId()).orElseThrow(()-> new RuntimeException(("Chatroom does not exist!")));

        Message message = new Message();
        message.setText(requestDto.getMessage());
        message.setUser(user);
        message.setChatroom(chatroom);
        message.setSentAt(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        MessageResponseDto messageResponseDto = new MessageResponseDto(savedMessage.getId(), savedMessage.getText(),
                user.getUsername(), chatroom.getId(), chatroom.getName(), savedMessage.getSentAt());


        if(chatroom.getType().equals(ChatType.GROUP)){
            messagingTemplate.convertAndSend("/topic/chatroom/" + chatroom.getId(), messageResponseDto);
        }else{
            ChatRoomMembership membership = chatroom.getMemberships().stream()
                    .filter(m -> !m.getUser().getId().equals(user.getId()))
                    .findFirst()
                    .orElseThrow(()->new RuntimeException("Recipient not found!"));


            messagingTemplate.convertAndSendToUser(membership.getUser().getUsername(), "/queue/messages", messageResponseDto);

        }

        return  messageResponseDto;


    }
}
