package com.kaliv.myths.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.ExceptionMessages.RESOURCE_ALREADY_EXISTS;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistsException extends RuntimeException {
    private final String resourceName;

    public ResourceAlreadyExistsException(String resourceName) {
        super(String.format(RESOURCE_ALREADY_EXISTS, resourceName));
        this.resourceName = resourceName;
    }
}
