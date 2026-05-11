package com.chatAPI.controller;


import com.chatAPI.dtos.UserResponseDto;
import com.chatAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/ownProfile")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getOwnProfile(){
        return userService.getOwnProfile();
    }

    @GetMapping("/profiles")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/search/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto searchByUsername(@PathVariable("username") String username){
        return userService.searchByUsername(username);
    }


}
