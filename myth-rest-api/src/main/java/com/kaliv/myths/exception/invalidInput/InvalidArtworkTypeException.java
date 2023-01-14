package com.kaliv.myths.exception.invalidInput;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.messages.ExceptionMessages.INVALID_ARTWORK_TYPE;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidArtworkTypeException extends RuntimeException {
    public InvalidArtworkTypeException() {
        super(String.format(INVALID_ARTWORK_TYPE));
    }
}
