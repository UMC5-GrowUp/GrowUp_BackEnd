package Growup.spring.email.converter;

import Growup.spring.email.dto.EmailDtoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConverter {


    //엑세스 토큰 응답
    public static EmailDtoRes.emailAuthRes passwordAuthTokenRes(String token){
        return EmailDtoRes.emailAuthRes.builder()
                .accessToken(token)
                .build();
    }

}
