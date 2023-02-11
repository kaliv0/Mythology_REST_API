package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kaliv.myths.dto.userDtos.AddUserDto;
import com.kaliv.myths.dto.userDtos.RegisterUserDto;
import com.kaliv.myths.dto.userDtos.UserDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.users.Role;
import com.kaliv.myths.entity.users.User;
import com.kaliv.myths.util.Clock;

public class UserMapper {
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserMapper(ModelMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User dtoToRegisteredUser(RegisterUserDto userDto, Role role) {
        return User.builder()
                .password(encodePassword(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .joinDate(Clock.getZonedDateTime())
                .isActive(true)
                .isNotLocked(true)
                .role(role)
                .build();
    }

    public User dtoToAddedUser(AddUserDto userDto, Role role) {
        return User.builder()
                .password(encodePassword(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .joinDate(Clock.getZonedDateTime())
                .isActive(userDto.isActive())
                .isNotLocked(userDto.isNotLocked())
                .role(role)
                .build();
    }

    public UserDto userToDto(User user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setAuthorities(
                user.getRole()
                        .getAuthorities().stream()
                        .map(BaseEntity::getName).toArray(String[]::new));
        return userDto;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}