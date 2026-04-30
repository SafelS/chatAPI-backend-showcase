package com.chatAPI.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "E-Mail cannot be empty")
    @Email(message = "Please enter a valid e-mail")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6)
    private String password;

}
