package com.kaliv.myths.jwt;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.kaliv.myths.constant.params.Args.EE_TIME_ZONE;
import static com.kaliv.myths.constant.params.Args.JSON_DATE_TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
public class IdentityHttpResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JSON_DATE_TIME_PATTERN, timezone = EE_TIME_ZONE)
    private Date timeStamp;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;

    public IdentityHttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.timeStamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
    }
}
