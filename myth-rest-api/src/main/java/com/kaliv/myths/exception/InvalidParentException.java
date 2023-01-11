package com.kaliv.myths.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.messages.ExceptionMessages.INVALID_PARENT;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidParentException extends RuntimeException {
    public InvalidParentException() {
        super(String.format(INVALID_PARENT));
    }
}
