package com.kaliv.myths.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kaliv.myths.dto.userDtos.RegisterUserDto;

import static com.kaliv.myths.constant.SecurityConstants.NO_REPLY_ADDRESS;
import static com.kaliv.myths.constant.messages.ResponseMessages.SUCCESSFUL_REGISTER;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, RegisterUserDto userData) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(NO_REPLY_ADDRESS);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(String.format(SUCCESSFUL_REGISTER, userData.getUsername(), userData.getPassword()));
        emailSender.send(message);
    }
}
