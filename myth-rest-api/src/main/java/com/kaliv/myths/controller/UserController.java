package com.kaliv.myths.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.common.JWTTokenProvider;
import com.kaliv.myths.common.Role;
import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.dto.userDtos.AddUserDto;
import com.kaliv.myths.dto.userDtos.LoginUserDto;
import com.kaliv.myths.dto.userDtos.RegisterUserDto;
import com.kaliv.myths.dto.userDtos.UserDto;
import com.kaliv.myths.entity.domain.User;
import com.kaliv.myths.entity.domain.UserPrincipal;
import com.kaliv.myths.exception.security.ExceptionHandling;
import com.kaliv.myths.exception.security.domain.EmailExistException;
import com.kaliv.myths.exception.security.domain.UserNotFoundException;
import com.kaliv.myths.exception.security.domain.UsernameExistException;
import com.kaliv.myths.service.security.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import static com.kaliv.myths.constant.security.SecurityConstant.JWT_TOKEN_HEADER;

@Tag(name = "Users")
@RestController
@RequestMapping("/api/v1/users")
public class UserController extends ExceptionHandling {
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto userDto)
            throws EmailExistException, UsernameExistException {
        UserDto newUser = userService.register(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginUserDto userDto) {
        Tuple<User, UserPrincipal> userData = userService.login(userDto);
        User loginUser = userData.getFirst();
        UserPrincipal userPrincipal = userData.getSecond();
        HttpHeaders jwtHeader = this.getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> addNewUser(@Valid @RequestBody AddUserDto userDto)
            throws UserNotFoundException, UsernameExistException, EmailExistException {
        UserDto newUser = userService.addNewUser(userDto);
        return ResponseEntity.ok(newUser);
    }

//    @PostMapping("/update")
//    public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUsername,
//                                       @RequestParam("firstName") String firstName,
//                                       @RequestParam("lastName") String lastName,
//                                       @RequestParam("username") String username,
//                                       @RequestParam("email") String email,
//                                       @RequestParam("role") String role,
//                                       @RequestParam("isActive") String isActive,
//                                       @RequestParam("isNonLocked") String isNonLocked,
//                                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
//        User updatedUser = userService.updateUser(currentUsername, firstName, lastName, username, email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
//        return new ResponseEntity<>(updatedUser, OK);
//    }
//
//    @GetMapping("/find/{username}")
//    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
//        User user = userService.findUserByUsername(username);
//        return new ResponseEntity<>(user, OK);
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getUsers();
//        return new ResponseEntity<>(users, OK);
//    }
//
//    @GetMapping("/reset-password/{email}")
//    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws MessagingException, EmailNotFoundException {
//        userService.resetPassword(email);
//        return response(OK, EMAIL_SENT + email);
//    }
//
//    @DeleteMapping("/delete/{username}")
//    @PreAuthorize("hasAnyAuthority('user:delete')")
//    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
//        userService.deleteUser(username);
//        return response(OK, USER_DELETED_SUCCESSFULLY);
//    }
//
//    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
//        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
//                message), httpStatus);
//    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }
}
