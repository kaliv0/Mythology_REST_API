package com.kaliv.myths.service.login;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private static final long CACHE_EXPIRATION_PERIOD = 15;
    private final LoadingCache<String, Integer> loginAttemptCache;

    @Autowired
    public LoginAttemptServiceImpl() {
        super();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_EXPIRATION_PERIOD, MINUTES)
                .maximumSize(100).build(new CacheLoader<>() {
                    public Integer load(@NonNull String key) {
                        return 0;
                    }
                });
    }

    public void evictUserFromLoginAttemptCache(String username) {
        loginAttemptCache.invalidate(username);
    }

    public void addUserToLoginAttemptCache(String username) {
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        loginAttemptCache.put(username, attempts);
    }

    public boolean hasExceededMaxAttempts(String username) {
        try {
            return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
