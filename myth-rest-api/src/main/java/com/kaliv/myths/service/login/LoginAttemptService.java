package com.kaliv.myths.service.login;

public interface LoginAttemptService {
    void evictUserFromLoginAttemptCache(String username);

    void addUserToLoginAttemptCache(String username);

    boolean hasExceededMaxAttempts(String username);
}
