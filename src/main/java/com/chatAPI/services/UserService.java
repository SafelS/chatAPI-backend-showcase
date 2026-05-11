package com.chatAPI.services;

import com.chatAPI.dtos.UserResponseDto;
import com.chatAPI.entities.User;
import com.chatAPI.enums.Status;
import com.chatAPI.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public UserResponseDto getOwnProfile(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        return new UserResponseDto(user.getUsername(),user.getEmail(), user.getStatus());
    }

    public List<UserResponseDto> getAllUsers(){
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(u -> new UserResponseDto(u.getUsername(),u.getEmail(),u.getStatus())).toList();

        return users;
    }

    public UserResponseDto searchByUsername(String username){

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User does not exist!"));

        return new UserResponseDto(user.getUsername(), user.getEmail(), user.getStatus());

    }

    public void updateStatus(String username, Status status){

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
        user.setStatus(status);
        userRepository.save(user);

        messagingTemplate.convertAndSend("/topic/status",
                new UserResponseDto(user.getUsername(), user.getEmail(), status));


    }

}
