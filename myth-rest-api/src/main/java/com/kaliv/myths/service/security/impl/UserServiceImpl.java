package com.kaliv.myths.service.security.impl;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kaliv.myths.common.Role;
import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.dto.userDtos.AddUserDto;
import com.kaliv.myths.dto.userDtos.LoginUserDto;
import com.kaliv.myths.dto.userDtos.RegisterUserDto;
import com.kaliv.myths.dto.userDtos.UserDto;
import com.kaliv.myths.entity.domain.User;
import com.kaliv.myths.entity.domain.UserPrincipal;
import com.kaliv.myths.exception.security.domain.*;
import com.kaliv.myths.mapper.UserMapper;
import com.kaliv.myths.persistence.UserRepository;
import com.kaliv.myths.service.security.UserService;

import static com.kaliv.myths.constant.messages.ExceptionMessages.EMAIL_ALREADY_EXISTS;
import static com.kaliv.myths.constant.messages.ExceptionMessages.NO_USER_FOUND_BY_USERNAME;
import static com.kaliv.myths.constant.messages.ExceptionMessages.USERNAME_ALREADY_EXISTS;
import static com.kaliv.myths.constant.security.FileConstant.DEFAULT_USER_IMAGE_PATH;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto register(RegisterUserDto userDto) throws UsernameExistException, EmailExistException {
        validateNewUserCredentials(userDto.getUsername(), userDto.getEmail());
        User user = userMapper.dtoToRegisteredUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.userToDto(savedUser);
    }

    @Override
    public Tuple<User, UserPrincipal> login(LoginUserDto userDto) {
        this.authenticateUser(userDto.getUsername(), userDto.getPassword());
        Optional<User> loginUser = this.findUserByUsername(userDto.getUsername());
        if (loginUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format(NO_USER_FOUND_BY_USERNAME, userDto.getUsername()));
        }
        User userValue = loginUser.get();
        UserPrincipal userPrincipal = new UserPrincipal(userValue);
        return new Tuple<>(userValue, userPrincipal);
    }

    @Override
    public UserDto addNewUser(AddUserDto userDto) throws EmailExistException, UsernameExistException {
        this.validateNewUserCredentials(userDto.getUsername(), userDto.getEmail());
        User user = userMapper.dtoToAddedUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.userToDto(savedUser);
    }

//    @Override
//    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
//        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
//        currentUser.setFirstName(newFirstName);
//        currentUser.setLastName(newLastName);
//        currentUser.setUsername(newUsername);
//        currentUser.setEmail(newEmail);
//        currentUser.setActive(isActive);
//        currentUser.setNotLocked(isNonLocked);
//        currentUser.setRole(getRoleEnumName(role).name());
//        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
//        userRepository.save(currentUser);
//        return currentUser;
//    }

//    @Override
//    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
//        User user = userRepository.findUserByEmail(email);
//        if (user == null) {
//            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
//        }
//        String password = generatePassword();
//        user.setPassword(encodePassword(password));
//        userRepository.save(user);
////        emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());
//        emailService.sendVerificationEmail(user.getFirstName(), password, user.getEmail());
//    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        return null;
    }

    @Override
    public void deleteUser(String username) throws IOException {

    }

    @Override
    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {

    }

//    @Override
//    public void deleteUser(String username) throws IOException {
//        User user = userRepository.findUserByUsername(username);
//        Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
//        FileUtils.deleteDirectory(new File(userFolder.toString()));
//        userRepository.deleteById(user.getId());
//    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private void validateNewUserCredentials(String newUsername, String newEmail)
            throws UsernameExistException, EmailExistException {
        Optional<User> userByNewUsername = this.findUserByUsername(newUsername);
        Optional<User> userByNewEmail = this.findUserByEmail(newEmail);
        if (userByNewUsername.isPresent()) {
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        if (userByNewEmail.isPresent()) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
    }

//    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
//            throws UserNotFoundException, UsernameExistException, EmailExistException {
//        Optional<User> userByNewUsername = findUserByUsername(newUsername);
//        Optional<User> userByNewEmail = findUserByEmail(newEmail);
//        if (StringUtils.isNotBlank(currentUsername)) {
//            User currentUser = findUserByUsername(currentUsername);
//            if (currentUser == null) {
//                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
//            }
//            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
//                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
//            }
//            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
//                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
//            }
//            return currentUser;
//        }
//        if (userByNewUsername != null) {
//            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
//        }
//        if (userByNewEmail != null) {
//            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
//        }
//        return null;
//    }

    private Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    private void authenticateUser(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
