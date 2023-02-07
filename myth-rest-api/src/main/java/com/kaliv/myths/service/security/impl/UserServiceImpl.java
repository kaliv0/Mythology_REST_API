package com.kaliv.myths.service.security.impl;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kaliv.myths.common.Role;
import com.kaliv.myths.common.UserMapper;
import com.kaliv.myths.dto.userDtos.RegisterUserDto;
import com.kaliv.myths.entity.domain.User;
import com.kaliv.myths.entity.domain.UserPrincipal;
import com.kaliv.myths.exception.security.domain.*;
import com.kaliv.myths.persistence.UserRepository;
import com.kaliv.myths.service.security.EmailService;
import com.kaliv.myths.service.security.LoginAttemptService;
import com.kaliv.myths.service.security.UserService;

import static com.kaliv.myths.constant.messages.ExceptionMessages.EMAIL_ALREADY_EXISTS;
import static com.kaliv.myths.constant.messages.ExceptionMessages.NO_USER_FOUND_BY_USERNAME;
import static com.kaliv.myths.constant.messages.ExceptionMessages.USERNAME_ALREADY_EXISTS;
import static com.kaliv.myths.constant.security.FileConstant.DEFAULT_USER_IMAGE_PATH;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           LoginAttemptService loginAttemptService,
                           EmailService emailService,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException(String.format(NO_USER_FOUND_BY_USERNAME, username));
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            return userPrincipal;
        }
    }

    @Override
    public User register(RegisterUserDto userDto) throws UsernameExistException, EmailExistException {
        validateNewUserCredentials(userDto.getUsername(), userDto.getEmail());

        User user = userMapper.dtoToRegisteredUser(userDto);
        User savedUser = userRepository.save(user);
        //TODO: map to dto
        return savedUser;
    }

//    @Override
//    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
//        validateNewUsernameAndEmail(EMPTY, username, email);
//        User user = new User();
//        String password = generatePassword();
//        user.setUserId(generateUserId());
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setJoinDate(new Date());
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPassword(encodePassword(password));
//        user.setActive(isActive);
//        user.setNotLocked(isNonLocked);
//        user.setRole(getRoleEnumName(role).name());
//        user.setAuthorities(getRoleEnumName(role).getAuthorities());
//        userRepository.save(user);
//        return user;
//    }

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
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        return null;
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

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private void validateNewUserCredentials(String newUsername, String newEmail)
            throws UsernameExistException, EmailExistException {
        Optional<User> userByNewUsername = findUserByUsername(newUsername);
        Optional<User> userByNewEmail = findUserByEmail(newEmail);
        if (userByNewUsername.isPresent()) {
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        if (userByNewEmail.isPresent()) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
    }

//    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
//            throws UserNotFoundException, UsernameExistException, EmailExistException {
//        User userByNewUsername = findUserByUsername(newUsername);
//        User userByNewEmail = findUserByEmail(newEmail);
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
}
