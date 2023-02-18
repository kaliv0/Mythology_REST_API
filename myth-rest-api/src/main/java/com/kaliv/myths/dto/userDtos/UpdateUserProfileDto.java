package com.kaliv.myths.dto.userDtos;

import com.kaliv.myths.util.validator.email.ValidEmail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserProfileDto {
    String firstName;
    String lastName;
    String password;
    @ValidEmail
    String email;
}
