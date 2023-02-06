package com.kaliv.myths.controller;

import javax.mail.MessagingException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaliv.myths.common.JWTTokenProvider;
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
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;

    public UserController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user)
            throws UserNotFoundException, EmailExistException, MessagingException, UsernameExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

//    @PostMapping("/add")
//    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
//                                           @RequestParam("lastName") String lastName,
//                                           @RequestParam("username") String username,
//                                           @RequestParam("email") String email,
//                                           @RequestParam("role") String role,
//                                           @RequestParam("isActive") String isActive,
//                                           @RequestParam("isNonLocked") String isNonLocked,
//                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
//        User newUser = userService.addNewUser(firstName, lastName, username, email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
//        return new ResponseEntity<>(newUser, OK);
//    }
//
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

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
