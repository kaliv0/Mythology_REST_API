package com.kaliv.myths.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

import static com.kaliv.myths.constant.ExceptionMessages.DUPLICATE_ENTRIES;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateEntriesException extends RuntimeException {
    private final String firstFieldName;
    private final String secondFieldName;

    public DuplicateEntriesException(String firstFieldName, String secondFieldName) {
        super(String.format(DUPLICATE_ENTRIES, firstFieldName, secondFieldName));
        this.firstFieldName = firstFieldName;
        this.secondFieldName = secondFieldName;
    }
}
