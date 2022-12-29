package com.kaliv.myths.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.ExceptionMessages.RESOURCE_NOT_FOUND;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;

    public ResourceNotFoundException(String resourceName) {
        super(String.format(RESOURCE_NOT_FOUND, resourceName));
        this.resourceName = resourceName;
    }
}
