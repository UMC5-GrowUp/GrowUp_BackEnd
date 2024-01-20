package Growup.spring.member.service;

import Growup.spring.member.model.User;
import Growup.spring.email.dto.EmailDtoReq;
import Growup.spring.member.dto.UserDtoReq;
import Growup.spring.member.dto.UserDtoRes;

import javax.mail.MessagingException;

public interface UserService {

    User signUp(UserDtoReq.userRegisterReq request);

    void signUpAuth(String certificationNumber,String email);

    UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request);

    String inVaildToken(String refreshToken);
    String passworAuthdToken(String certificationNumber,String email);

    User pwRestore(UserDtoReq.passwordRestoreReq request,Long userId);

    void sendEmailAuth(EmailDtoReq.emailAuthReq request,String text) throws MessagingException;

    User verifyEmail(String certificationNumber, String email);

}
