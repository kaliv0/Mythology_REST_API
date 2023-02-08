package com.kaliv.myths.service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.Tuple;
import com.kaliv.myths.dto.userDtos.*;
import com.kaliv.myths.entity.user.Role;
import com.kaliv.myths.entity.user.User;
import com.kaliv.myths.entity.user.UserPrincipal;
import com.kaliv.myths.exception.alreadyExists.EmailExistException;
import com.kaliv.myths.exception.alreadyExists.UsernameExistException;
import com.kaliv.myths.mapper.UserMapper;
import com.kaliv.myths.persistence.UserRepository;

import static com.kaliv.myths.constant.messages.ExceptionMessages.EMAIL_ALREADY_EXISTS;
import static com.kaliv.myths.constant.messages.ExceptionMessages.NO_USER_FOUND;
import static com.kaliv.myths.constant.messages.ExceptionMessages.USERNAME_ALREADY_EXISTS;

@Service
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
    public UserDto register(RegisterUserDto userDto) throws EmailExistException, UsernameExistException {
        this.validateUserCredentials(userDto.getUsername(), userDto.getEmail());
        User user = userMapper.dtoToRegisteredUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.userToDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(NO_USER_FOUND, username)));
        return userMapper.userToDto(user);
    }

    @Override
    public Tuple<UserDto, UserPrincipal> login(LoginUserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        User loginUser = userRepository.findUserByUsername(userDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(NO_USER_FOUND, userDto.getUsername())));
        UserDto userValue = userMapper.userToDto(loginUser);
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        return new Tuple<>(userValue, userPrincipal);
    }

    @Override
    public UserDto addNewUser(AddUserDto userDto) throws EmailExistException, UsernameExistException {
        this.validateUserCredentials(userDto.getUsername(), userDto.getEmail());
        User user = userMapper.dtoToAddedUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(String username, UpdateUserDto userDto) throws EmailExistException {
        User userToUpdate = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(NO_USER_FOUND, username)));

        this.updateBasicUserCredentials(userDto, userToUpdate);

        Boolean isActive = userDto.isActive();
        Boolean isNotLocked = userDto.isNotLocked();
        Role role = userDto.getRole();
        if (isActive != null && isActive != userToUpdate.isActive()) {
            userToUpdate.setActive(isActive);
        }
        if (isNotLocked != null && isNotLocked != userToUpdate.isNotLocked()) {
            userToUpdate.setNotLocked(isNotLocked);
        }
        if (role != null && !role.name().equals(userToUpdate.getRole())) {
            userToUpdate.setRole(role.name());
            userToUpdate.setAuthorities(role.getAuthorities());
        }
        userRepository.save(userToUpdate);
        return userMapper.userToDto(userToUpdate);
    }

    @Override
    public UserDto updateProfile(UpdateUserProfileDto userDto) throws EmailExistException {
        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
        User loginUser = userRepository.findUserByUsername(currentUsername)
                .orElseThrow();

        this.updateBasicUserCredentials(userDto, loginUser);
        userRepository.save(loginUser);
        return userMapper.userToDto(loginUser);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(NO_USER_FOUND, username)));
        userRepository.delete(user);
    }

    private void updateBasicUserCredentials(UpdateUserProfileDto userDto, User user) throws EmailExistException {
        String firstName = userDto.getFirstName();
        String lastName = userDto.getLastName();
        String password = userDto.getPassword();
        String email = userDto.getEmail();
        if (StringUtils.isNotBlank(firstName) && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }
        if (StringUtils.isNotBlank(lastName) && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }
        if (StringUtils.isNotBlank(password) && !password.equals(user.getPassword())) {
            user.setPassword(password);
        }
        if (StringUtils.isNotBlank(email) && !email.equals(user.getEmail())) {
            Optional<User> userByEmail = userRepository.findUserByEmail(email);
            if (userByEmail.isPresent()) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            user.setEmail(email);
        }
    }

    private void validateUserCredentials(String username, String email)
            throws UsernameExistException, EmailExistException {
        Optional<User> userByUsername = userRepository.findUserByUsername(username);
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByUsername.isPresent()) {
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        if (userByEmail.isPresent()) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
    }
}
