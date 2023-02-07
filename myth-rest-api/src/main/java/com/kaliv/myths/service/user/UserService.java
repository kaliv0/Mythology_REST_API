package com.kaliv.myths.service.user;

import java.util.List;

import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.dto.userDtos.*;
import com.kaliv.myths.entity.user.UserPrincipal;
import com.kaliv.myths.exception.alreadyExists.EmailExistException;
import com.kaliv.myths.exception.alreadyExists.UsernameExistException;

public interface UserService {
    UserDto register(RegisterUserDto userDto) throws EmailExistException, UsernameExistException;

    Tuple<UserDto, UserPrincipal> login(LoginUserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserByUsername(String username);

    UserDto addNewUser(AddUserDto userDto) throws UsernameExistException, EmailExistException;

//    User updateUser(UpdateUserDto userDto);

    UserDto updateProfile(UpdateUserProfileDto userDto);

    void deleteUser(String username);
}
