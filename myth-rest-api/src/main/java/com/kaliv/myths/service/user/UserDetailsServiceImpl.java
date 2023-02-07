package com.kaliv.myths.service.user;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kaliv.myths.entity.user.User;
import com.kaliv.myths.entity.user.UserPrincipal;
import com.kaliv.myths.persistence.UserRepository;
import com.kaliv.myths.service.login.LoginAttemptService;
import com.kaliv.myths.util.Clock;

import static com.kaliv.myths.constant.messages.ExceptionMessages.NO_USER_FOUND;

@Service
@Qualifier("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public UserDetailsServiceImpl(UserRepository userRepository,
                                  LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(NO_USER_FOUND, username)));
        this.validateLoginAttempt(user);
        user.setLastLoginDate(user.getCurrentLoginDate());
        user.setCurrentLoginDate(Clock.getZonedDateTime());
        userRepository.save(user);
        return new UserPrincipal(user);
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            user.setNotLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
