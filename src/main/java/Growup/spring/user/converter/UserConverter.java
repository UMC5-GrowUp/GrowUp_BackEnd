package Growup.spring.user.converter;

import Growup.spring.user.dto.RefreshTokenRes;
import Growup.spring.user.dto.UserDtoReq;
import Growup.spring.user.dto.UserDtoRes;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConverter {


    //회원가입 응답
    public static UserDtoRes.userRegisterRes userDtoRes(User user){
        return UserDtoRes.userRegisterRes.builder()
                .userId(user.getId())
                .createdAt(user.getCreatedAt())
                .build();
    }

    //로그인 응답
    public static UserDtoRes.userLoginRes userLoginRes(User user, String accessToken, String refreshToken){
        return UserDtoRes.userLoginRes.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }

    //accessToken 재발급 응답
    public static RefreshTokenRes refreshTokenRes(String acceesToken){
        return RefreshTokenRes.builder()
                .newAccessToken(acceesToken)
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

    public static UserDtoRes.infoRes info(User user){
        return UserDtoRes.infoRes.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .photoUrl(user.getPhotoUrl())
                .build();
    }

    public static UserDtoRes.photoChangeRes photoChangeRes(User user){
        return UserDtoRes.photoChangeRes.builder()
                .userId(user.getId())
                .photoUrl(user.getPhotoUrl())
                .build();
    }
}
