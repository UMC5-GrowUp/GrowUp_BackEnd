package Growup.spring.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


public class UserDtoReq {

    @Setter
    @Getter
    public static class userRegisterReq{

        @NotBlank
        private String name;
        @NotBlank
        private String nickName;
        @NotBlank
        private String email;
        @NotBlank
        private String password;


    }
    @Getter
    public static class userLoginReq{
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class passwordRestoreReq{
        @NotBlank
        private String password;
        @NotBlank
        private String passwordCheck;
    }
}
