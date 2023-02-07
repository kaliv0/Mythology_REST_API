package com.kaliv.myths.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaliv.myths.common.IdentityHttpResponse;

import static com.kaliv.myths.constant.messages.ExceptionMessages.FORBIDDEN_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        IdentityHttpResponse httpResponse = new IdentityHttpResponse(FORBIDDEN.value(),
                FORBIDDEN, FORBIDDEN.getReasonPhrase().toUpperCase(),
                FORBIDDEN_MESSAGE);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());

        try (OutputStream outputStream = response.getOutputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(outputStream, httpResponse);
//        outputStream.flush();
        }
    }
}
