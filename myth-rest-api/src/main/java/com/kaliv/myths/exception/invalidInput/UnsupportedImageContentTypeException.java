package com.kaliv.myths.exception.invalidInput;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.messages.ExceptionMessages.UNSUPPORTED_IMAGE_CONTENT_TYPE;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnsupportedImageContentTypeException extends RuntimeException {
    public UnsupportedImageContentTypeException() {
        super(String.format(UNSUPPORTED_IMAGE_CONTENT_TYPE));
    }
}
