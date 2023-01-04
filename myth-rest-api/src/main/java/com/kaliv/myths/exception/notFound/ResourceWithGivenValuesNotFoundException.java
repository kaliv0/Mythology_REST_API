package com.kaliv.myths.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.messages.ExceptionMessages.RESOURCE_WITH_GIVEN_VALUE_NOT_FOUND;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceWithGivenValuesNotFoundException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final long fieldValue;

    public ResourceWithGivenValuesNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format(RESOURCE_WITH_GIVEN_VALUE_NOT_FOUND, resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
