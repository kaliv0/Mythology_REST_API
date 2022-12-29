package com.kaliv.myths.exception.alreadyExists;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.ExceptionMessages.RESOURCE_WITH_GIVEN_VALUE_ALREADY_EXISTS;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceWithGivenValuesExistsException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final String fieldValue;

    public ResourceWithGivenValuesExistsException(String resourceName, String fieldName, String fieldValue) {
        super(String.format(RESOURCE_WITH_GIVEN_VALUE_ALREADY_EXISTS, resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
