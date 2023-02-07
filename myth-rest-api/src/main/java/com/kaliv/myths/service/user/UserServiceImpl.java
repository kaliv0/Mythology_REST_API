package com.kaliv.myths.service.user;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.container.Tuple;
import com.kaliv.myths.dto.userDtos.*;
import com.kaliv.myths.entity.user.User;
import com.kaliv.myths.entity.user.UserPrincipal;
import com.kaliv.myths.exception.alreadyExists.EmailExistException;
import com.kaliv.myths.exception.alreadyExists.UsernameExistException;
import com.kaliv.myths.mapper.UserMapper;
import com.kaliv.myths.persistence.UserRepository;

import static com.kaliv.myths.constant.messages.ExceptionMessages.EMAIL_ALREADY_EXISTS;
import static com.kaliv.myths.constant.messages.ExceptionMessages.NO_USER_FOUND_BY_USERNAME;
import static com.kaliv.myths.constant.messages.ExceptionMessages.USERNAME_ALREADY_EXISTS;

@Service
//@Transactional //TODO: check if needed
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
    public Tuple<UserDto, UserPrincipal> login(LoginUserDto userDto) {
        this.authenticateUser(userDto.getUsername(), userDto.getPassword());
        Optional<User> loginUser = userRepository.findUserByUsername(userDto.getUsername());
        if (loginUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format(NO_USER_FOUND_BY_USERNAME, userDto.getUsername()));
        }
        UserDto userValue = userMapper.userToDto(loginUser.get());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser.get());
        return new Tuple<>(userValue, userPrincipal);
    }

    @Override
    public UserDto addNewUser(AddUserDto userDto) throws UsernameExistException, EmailExistException {
        this.validateNewUserCredentials(userDto.getUsername(), userDto.getEmail());
        User user = userMapper.dtoToAddedUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.userToDto(savedUser);
    }

//    @Override
//    public User updateUser(UpdateUserDto userDto)
//            throws UserNotFoundException, UsernameExistException, EmailExistException {
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

    @Override
    public UserDto updateProfile(UpdateUserProfileDto userDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User loginUser = userRepository.findUserByUsername(currentUsername).orElseThrow();
        if (userDto.getFirstName()!= null) {
            loginUser.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            loginUser.setLastName(userDto.getLastName());
        }
        if (userDto.getPassword() != null) {
            loginUser.setPassword(userDto.getPassword());
        }
        if (userDto.getEmail() != null) {
            loginUser.setEmail(userDto.getEmail());
        }
        userRepository.save(loginUser);
        return userMapper.userToDto(loginUser);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String username) throws IOException {

    }

//    @Override
//    public void deleteUser(String username) throws IOException {
//        User user = userRepository.findUserByUsername(username);
//        Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
//        FileUtils.deleteDirectory(new File(userFolder.toString()));
//        userRepository.deleteById(user.getId());
//    }

    private void validateNewUserCredentials(String newUsername, String newEmail)
            throws UsernameExistException, EmailExistException {
        Optional<User> userByNewUsername = userRepository.findUserByUsername(newUsername);
        Optional<User> userByNewEmail = userRepository.findUserByEmail(newEmail);
        if (userByNewUsername.isPresent()) {
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        if (userByNewEmail.isPresent()) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
    }
//
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

    private void authenticateUser(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
