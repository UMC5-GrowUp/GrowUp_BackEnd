package Growup.spring.security.handler;

import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        log.error("exception : " + exception);

        /**
         * 토큰 없는 경우
         */
        if (exception == null) {
            log.info("[NULL TOKEN]");
            JwtAuthenticationFilter.setErrorResponse(response, ErrorStatus.JWT_EMPTY); // 토큰이 없음
        }

        if (exception.equals("로그아웃 되었습니다. 로그인하세요.")) {
            log.info("[LOGOUT TOKEN]");
            JwtAuthenticationFilter.setErrorResponse(response, ErrorStatus.JWT_INVALID); // 토큰이 없음
        }
    }
}
