package com.kaliv.myths.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.ExceptionMessages.RESOURCE_LIST_NOT_FOUND;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceListNotFoundException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;

    public ResourceListNotFoundException(String resourceName, String fieldName) {
        super(String.format(RESOURCE_LIST_NOT_FOUND, resourceName, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }
}
