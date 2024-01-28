package Growup.spring.user.service;

import Growup.spring.user.model.User;
import Growup.spring.user.dto.UserDtoReq;
import Growup.spring.user.dto.UserDtoRes;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User signUp(UserDtoReq.userRegisterReq request);

    void signUpAuth(String certificationNumber,String email);

    UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request);

    String inVaildToken(String refreshToken);
    String passwordAuthToken(String certificationNumber,String email);

    User pwRestore(UserDtoReq.passwordRestoreReq request,Long userId);

    void sendEmailAuth(String email,String text);

    User verifyEmail(String certificationNumber, String email);

    void logout(String accessToken);

    void emailChange(String email,Long userId);
    void currentPasswordCheckReq(Long userId, UserDtoReq.currentPasswordCheckReq request);

    void withdraw(Long userId, String currentPwd);

    UserDtoRes.infoRes info(Long userId);

    User photoChange(MultipartFile photoImage, Long userId);
    void changeNickname(String nickName,Long userId);





}
