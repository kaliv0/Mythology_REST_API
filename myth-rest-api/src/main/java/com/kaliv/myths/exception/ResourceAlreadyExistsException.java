package com.kaliv.myths.exception;

import static com.kaliv.myths.constant.ExceptionMessages.RESOURCE_ALREADY_EXISTS;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistsException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final String fieldValue;

    public ResourceAlreadyExistsException(String resourceName, String fieldName, String fieldValue) {
        super(String.format(RESOURCE_ALREADY_EXISTS, resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
