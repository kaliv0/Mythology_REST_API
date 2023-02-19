package com.kaliv.myths.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kaliv.myths.entity.users.User;

import static com.kaliv.myths.constant.EmailConstants.*;
import static com.kaliv.myths.constant.messages.ResponseMessages.EMAIL_CONFIRMATION;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String subject, String actionType, User user) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(NO_REPLY_ADDRESS);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(String.format(
                EMAIL_CONFIRMATION,
                actionType,
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getRole().getName(),
                user.isActive() ? ACTIVE : INACTIVE,
                user.isNotLocked() ? UNLOCKED : LOCKED
        ));
        emailSender.send(message);
    }
}
