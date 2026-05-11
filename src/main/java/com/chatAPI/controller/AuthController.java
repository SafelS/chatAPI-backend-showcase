package com.chatAPI.controller;

import com.chatAPI.dtos.AuthResponseDto;
import com.chatAPI.dtos.LoginRequestDto;
import com.chatAPI.dtos.RegisterRequestDto;
import com.chatAPI.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto registerUser(@Valid @RequestBody RegisterRequestDto requestDto){
        return authService.registerUser(requestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDto loginUser(@Valid @RequestBody LoginRequestDto requestDto){
        return authService.loginUser(requestDto);
    }

}
