package com.chatAPI.controller;


import com.chatAPI.dtos.ChatroomRequestDto;
import com.chatAPI.dtos.ChatroomResponseDto;
import com.chatAPI.services.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @PostMapping("/createGroupChat")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatroomResponseDto createGroupChat(@RequestBody ChatroomRequestDto requestDto){
        return chatroomService.createGroupChat(requestDto);
    }

    @PostMapping("/createPrivateChat/{otherUsername}")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatroomResponseDto findOrCreatePrivateChat(@PathVariable String otherUsername){
        return chatroomService.findOrCreatePrivateChat(otherUsername);
    }

    @PutMapping("/joinGroup/{groupName}")
    @ResponseStatus(HttpStatus.OK)
    public ChatroomResponseDto joinGroupChat(@PathVariable String groupName){
        return chatroomService.joinGroupChat(groupName);
    }

    @PutMapping("/leaveGroup/{groupName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveGroupChat(@PathVariable String groupName){
        chatroomService.leaveGroupChat(groupName);
    }

    @GetMapping("/getUserChats")
    @ResponseStatus(HttpStatus.OK)
    public List<ChatroomResponseDto> getUserChats(){
        return chatroomService.getUserChats();
    }

}
