package com.kaliv.myths.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.userDtos.*;
import com.kaliv.myths.entity.user.UserPrincipal;
import com.kaliv.myths.exception.alreadyExists.EmailExistException;
import com.kaliv.myths.exception.alreadyExists.UsernameExistException;
import com.kaliv.myths.exception.notFound.UserNotFoundException;
import com.kaliv.myths.jwt.JwtTokenProvider;
import com.kaliv.myths.service.user.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterUserDto userDto)
            throws EmailExistException, UsernameExistException {
        UserDto newUser = userService.register(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginUserDto userDto) {
        Tuple<UserDto, UserPrincipal> userData = userService.login(userDto);
        UserDto loginUser = userData.getFirst();
        UserPrincipal userPrincipal = userData.getSecond();
        HttpHeaders jwtHeader = jwtTokenProvider.getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> addNewUser(@RequestBody AddUserDto userDto)
            throws UsernameExistException, EmailExistException {
        UserDto newUser = userService.addNewUser(userDto);
        return ResponseEntity.ok(newUser);
    }
//    @PatchMapping
//    public ResponseEntity<User> update(@RequestBody UpdateUserDto userDto)
//            throws UserNotFoundException, UsernameExistException, EmailExistException {
//        User updatedUser = userService.updateUser(userDto);
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

//    }

    @PatchMapping("/update-profile")
    public ResponseEntity<UserDto> updateProfile(UpdateUserProfileDto userDto) {
        UserDto updatedUser = userService.updateProfile(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username}")
//    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username)
            throws UserNotFoundException {
        userService.deleteUser(username);
        return new ResponseEntity<>(ResponseMessages.USER_DELETED, HttpStatus.OK);
    }
}
