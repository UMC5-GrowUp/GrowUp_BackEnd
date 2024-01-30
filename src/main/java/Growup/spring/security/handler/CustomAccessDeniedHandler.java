package Growup.spring.security.handler;

import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j

public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("[ACCESS DENIED]");

        JwtAuthenticationFilter.setErrorResponse(response, ErrorStatus.JWT_AUTHORIZATION_FAILED); //권한이 없음.
        return;

    }
}
