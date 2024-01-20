package Growup.spring.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class UserDtoReq {

    @Setter
    @Getter
    public static class userRegisterReq{

        @NotBlank(message = "이름은 공백일 수 없습니다.")
        private String name;
        @Size(message = "닉네임은 2글자 이상, 10글자 이하입니다.", min= 2, max = 10)
        private String nickName;
        private String email;
        private String password;


    }
    @Getter
    public static class userLoginReq{
        @NotBlank(message = "이름은 공백일 수 없습니다.")
        private String email;
        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
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
