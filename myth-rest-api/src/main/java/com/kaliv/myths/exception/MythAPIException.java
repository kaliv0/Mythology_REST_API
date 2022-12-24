package com.kaliv.myths.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MythAPIException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final String fieldValue;

    public MythAPIException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    //TODO=> check if necessary
//    public MythAPIException(String message, String message1) {
//        super(message);
//        this.message = message1;
//    }
}
