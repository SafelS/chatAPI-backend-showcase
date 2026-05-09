package com.chatAPI.services;


import com.chatAPI.dtos.ChatroomRequestDto;
import com.chatAPI.dtos.ChatroomResponseDto;
import com.chatAPI.entities.ChatRoomMembership;
import com.chatAPI.entities.Chatroom;
import com.chatAPI.entities.User;
import com.chatAPI.enums.ChatType;
import com.chatAPI.repositories.ChatroomMembershipRepository;
import com.chatAPI.repositories.ChatroomRepository;
import com.chatAPI.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.processing.CheckHQL;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomService {


    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;
    private final ChatroomMembershipRepository chatroomMembershipRepository;


    //Used for createGroupChat() & findOrCreatePrivateChat()
    private Chatroom createChatroom(String chatName, ChatType type, User user){

        Chatroom chatroom = new Chatroom();
        chatroom.setType(type);
        chatroom.setName(chatName);
        chatroomRepository.save(chatroom);

        ChatRoomMembership chatRoomMembership = new ChatRoomMembership();
        chatRoomMembership.setUser(user);
        chatRoomMembership.setChatroom(chatroom);
        chatRoomMembership.setJoinedAt(LocalDateTime.now());
        chatroomMembershipRepository.save(chatRoomMembership);


        return chatroom;
        //return new ChatroomResponseDto(chatroom.getId(), chatroom.getName(), chatroom.getType());


    }

    public ChatroomResponseDto createGroupChat(ChatroomRequestDto requestDto){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        Chatroom chatroom = createChatroom(requestDto.getChatName(), ChatType.GROUP, user);

        return new ChatroomResponseDto(chatroom.getId(), chatroom.getName(), chatroom.getType());


    }

    public ChatroomResponseDto findOrCreatePrivateChat(String otherUsername){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userA = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
        User userB = userRepository.findByUsername(otherUsername).orElseThrow(() -> new RuntimeException("Other user was not found!"));

        Optional<Chatroom> existingChat = chatroomRepository.findPrivateChatBetweenUsers(userA.getId(), userB.getId(), ChatType.PRIVATE);


        if (existingChat.isPresent()){
            Chatroom chatroom = existingChat.get();
            return new ChatroomResponseDto(chatroom.getId(),chatroom.getName(),chatroom.getType());
        }else {
            Chatroom newChatroom = createChatroom(null, ChatType.PRIVATE, userA);
            ChatRoomMembership membership = new ChatRoomMembership();
            membership.setUser(userB);
            membership.setChatroom(newChatroom);
            membership.setJoinedAt(LocalDateTime.now());
            chatroomMembershipRepository.save(membership);

            return new ChatroomResponseDto(newChatroom.getId(), newChatroom.getName(), newChatroom.getType());
        }

    }

    public ChatroomResponseDto joinGroupChat(String groupName){

        Chatroom chatroom = chatroomRepository.findByName(groupName).orElseThrow(() -> new RuntimeException("Chat not found!"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        if (chatroomMembershipRepository.existsByUserAndChatroom(user, chatroom)){
            throw new RuntimeException("User is already in group!");
        }

        ChatRoomMembership membership = new ChatRoomMembership();
        membership.setUser(user);
        membership.setChatroom(chatroom);
        membership.setJoinedAt(LocalDateTime.now());
        chatroomMembershipRepository.save(membership);

        return new ChatroomResponseDto(chatroom.getId(),chatroom.getName(),chatroom.getType());


    }

    public void leaveGroupChat(String groupName){

        Chatroom chatroom = chatroomRepository.findByName(groupName).orElseThrow(() -> new RuntimeException("Chat not found!"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        ChatRoomMembership membership = chatroomMembershipRepository.findByUserAndChatroom(user, chatroom)
                .orElseThrow(() -> new RuntimeException("User is not in Group or Group no longer exists!"));

        chatroomMembershipRepository.delete(membership);

    }

    public List<ChatroomResponseDto> getUserChats(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("USer not found"));

        List<ChatRoomMembership> memberships = chatroomMembershipRepository.findByUser(user);

        List<ChatroomResponseDto> chatrooms = memberships.stream()
                .map(m -> new ChatroomResponseDto(m.getChatroom().getId(),m.getChatroom().getName(), m.getChatroom().getType())).toList();

        return chatrooms;

    }




}
