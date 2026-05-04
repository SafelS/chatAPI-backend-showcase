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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageResponseDto createAndSendMessage(MessageRequestDto requestDto){

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

    public Page<MessageResponseDto> getMessages(int page, int size, Long chatroomId){

        Pageable pageable = PageRequest.of(page, size, Sort.by("sentAt").descending());

        return messageRepository.findByChatroomId(chatroomId, pageable)
                .map(m-> new MessageResponseDto(
                        m.getId(),
                        m.getText(),
                        m.getUser().getUsername(),
                        m.getChatroom().getId(),
                        m.getChatroom().getName(),
                        m.getSentAt()
                ));

    }
}
