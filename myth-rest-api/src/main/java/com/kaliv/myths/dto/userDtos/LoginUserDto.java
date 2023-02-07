package com.kaliv.myths.dto.userDtos;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
