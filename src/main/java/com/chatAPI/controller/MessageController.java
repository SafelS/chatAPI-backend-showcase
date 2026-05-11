package com.chatAPI.controller;


import com.chatAPI.dtos.MessageRequestDto;
import com.chatAPI.dtos.MessageResponseDto;
import com.chatAPI.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/getMessages")
    @ResponseStatus(HttpStatus.OK)
    public Page<MessageResponseDto> getMessages(@RequestParam int page, @RequestParam int size, @RequestParam Long chatroomId){
        return messageService.getMessages(page,size,chatroomId);
    }

}
