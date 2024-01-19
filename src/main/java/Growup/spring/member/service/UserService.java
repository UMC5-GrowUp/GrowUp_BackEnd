package Growup.spring.member.service;

import Growup.spring.member.model.User;
import Growup.spring.email.dto.EmailDtoReq;
import Growup.spring.member.dto.UserDtoReq;
import Growup.spring.member.dto.UserDtoRes;

public interface UserService {

    User signUp(UserDtoReq.userRegisterReq request);

    UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request);

    String inVaildToken(String refreshToken);

    User pwRestore(UserDtoReq.passwordRestoreReq request,Long userId);

    void sendEmailAuth(EmailDtoReq.emailAuthReq request);

    void verifyEmail(String certificationNumber, String email);
}
