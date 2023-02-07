package com.kaliv.myths.service.user;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.dto.userDtos.AddUserDto;
import com.kaliv.myths.dto.userDtos.LoginUserDto;
import com.kaliv.myths.dto.userDtos.RegisterUserDto;
import com.kaliv.myths.dto.userDtos.UserDto;
import com.kaliv.myths.entity.user.User;
import com.kaliv.myths.entity.user.UserPrincipal;
import com.kaliv.myths.exception.alreadyExists.EmailExistException;
import com.kaliv.myths.exception.alreadyExists.UsernameExistException;
import com.kaliv.myths.exception.notFound.EmailNotFoundException;
import com.kaliv.myths.exception.notFound.UserNotFoundException;

public interface UserService {

    UserDto register(RegisterUserDto userDto) throws UsernameExistException, EmailExistException;

    Tuple<User, UserPrincipal> login(LoginUserDto userDto);

    List<User> getUsers();

    Optional<User> findUserByUsername(String username);

    UserDto addNewUser(AddUserDto userDto) throws UserNotFoundException, UsernameExistException, EmailExistException;

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;

    void deleteUser(String username) throws IOException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundException;
}
