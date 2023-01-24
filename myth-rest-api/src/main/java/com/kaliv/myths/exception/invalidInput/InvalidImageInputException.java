package com.kaliv.myths.exception.invalidInput;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.messages.ExceptionMessages.INVALID_IMAGE;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidImageInputException extends RuntimeException {
    public InvalidImageInputException() {
        super(String.format(INVALID_IMAGE));
    }
}

