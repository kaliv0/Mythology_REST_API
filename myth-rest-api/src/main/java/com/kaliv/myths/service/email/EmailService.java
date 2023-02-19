package com.kaliv.myths.service.email;


import com.kaliv.myths.entity.users.User;

public interface EmailService {
    void sendSimpleMessage(String subject, String actionType, User user);
}
