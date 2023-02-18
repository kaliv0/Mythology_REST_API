package com.kaliv.myths.service.email;


import com.kaliv.myths.dto.userDtos.RegisterUserDto;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, RegisterUserDto userData);
}
