package Growup.spring.email.dto;

import lombok.*;

public class EmailDtoRes {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class emailAuthRes{
        private String accessToken;
    }
}
