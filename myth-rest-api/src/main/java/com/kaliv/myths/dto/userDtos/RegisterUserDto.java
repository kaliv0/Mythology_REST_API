package com.kaliv.myths.dto.userDtos;

import javax.validation.constraints.NotBlank;

import com.kaliv.myths.util.validator.email.ValidEmail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @ValidEmail
    private String email;
}
