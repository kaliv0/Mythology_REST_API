package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kaliv.myths.dto.userDtos.AddUserDto;
import com.kaliv.myths.dto.userDtos.RegisterUserDto;
import com.kaliv.myths.dto.userDtos.UserDto;
import com.kaliv.myths.entity.users.User;
import com.kaliv.myths.util.Clock;

import static com.kaliv.myths.entity.users.Role.ROLE_USER;

public class UserMapper {
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserMapper(ModelMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User dtoToRegisteredUser(RegisterUserDto userDto) {
        return User.builder()
                .password(encodePassword(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .joinDate(Clock.getZonedDateTime())
                .isActive(true)
                .isNotLocked(true)
                .role(ROLE_USER.name())
                .authorities(ROLE_USER.getAuthorities())
                .build();
    }

    public User dtoToAddedUser(AddUserDto userDto) {
        return User.builder()
                .password(encodePassword(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .joinDate(Clock.getZonedDateTime())
                .isActive(userDto.isActive())
                .isNotLocked(userDto.isNotLocked())
                .role(userDto.getRole().name())
                .authorities(userDto.getRole().getAuthorities())
                .build();
    }

    public UserDto userToDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}