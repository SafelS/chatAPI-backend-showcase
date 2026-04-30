package com.chatAPI.services;

import com.chatAPI.dtos.AuthResponseDto;
import com.chatAPI.dtos.LoginRequestDto;
import com.chatAPI.dtos.RegisterRequestDto;
import com.chatAPI.entities.User;
import com.chatAPI.enums.Status;
import com.chatAPI.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto registerUser(RegisterRequestDto requestDto){
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setStatus(Status.OFFLINE);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponseDto(token, "User created");

    }

    public AuthResponseDto loginUser(LoginRequestDto requestDto){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        return  new AuthResponseDto(token, "User logged in ");



    }


}
