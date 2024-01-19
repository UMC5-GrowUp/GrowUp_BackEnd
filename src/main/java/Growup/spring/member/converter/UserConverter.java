package Growup.spring.member.converter;

import Growup.spring.member.dto.RefreshTokenRes;
import Growup.spring.member.dto.UserDtoReq;
import Growup.spring.member.dto.UserDtoRes;
import Growup.spring.member.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConverter {


    //회원가입 응답
    public static UserDtoRes.userRegisterRes userDtoRes(User user){
        return UserDtoRes.userRegisterRes.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //로그인 응답
    public static UserDtoRes.userLoginRes userLoginRes(User user, String accessToken, String refreshToken){
        return UserDtoRes.userLoginRes.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .build();
    }

    //accessToken 재발급 응답
    public static RefreshTokenRes refreshTokenRes(String acceesToken){
        return RefreshTokenRes.builder()
                .accessToken(acceesToken)
                .build();
    }

    //passwordRestore 응답
    public static UserDtoRes.passwordRestoreRes passwordRestoreRes(User user){
        return UserDtoRes.passwordRestoreRes.builder()
                .userId(user.getId())
                .createdAt(user.getCreatedAt())
                .build();
    }

    //유저 객체 만들기
    public static User toUser(UserDtoReq.userRegisterReq request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .build();
    }
}
