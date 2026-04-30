package com.chatAPI.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password has to be at least 6 characters long")
    private String password;

    @NotBlank(message = "E-Mail cannot be empty")
    @Email(message = "Please enter a valid e-mail")
    private String email;


}
