package com.kaliv.myths.dto.userDtos;

import javax.validation.constraints.Email;

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
    @Email
    String email;
}
