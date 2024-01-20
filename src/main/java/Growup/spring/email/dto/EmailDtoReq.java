package Growup.spring.email.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class EmailDtoReq {

    @Getter
    public static class emailAuthReq{
        @NotBlank
        private String email;
    }


}
