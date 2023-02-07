package com.kaliv.myths.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaliv.myths.common.IdentityHttpResponse;

import static com.kaliv.myths.constant.messages.ExceptionMessages.ACCESS_DENIED_MESSAGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        IdentityHttpResponse httpResponse = new IdentityHttpResponse(UNAUTHORIZED.value(), UNAUTHORIZED,
                UNAUTHORIZED.getReasonPhrase().toUpperCase(), ACCESS_DENIED_MESSAGE);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());

        try (OutputStream outputStream = response.getOutputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(outputStream, httpResponse);
//        outputStream.flush();
        }
    }
}
