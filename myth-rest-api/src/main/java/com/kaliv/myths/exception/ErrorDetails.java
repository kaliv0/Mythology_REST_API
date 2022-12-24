package com.kaliv.myths.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final String details;
}
