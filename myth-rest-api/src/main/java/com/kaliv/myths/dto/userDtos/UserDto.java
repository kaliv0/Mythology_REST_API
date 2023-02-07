package com.kaliv.myths.dto.userDtos;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private ZonedDateTime joinDate;
    private String role;
    private String[] authorities;
}
