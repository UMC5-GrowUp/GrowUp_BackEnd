package Growup.spring.member.Controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.email.converter.EmailConverter;
import Growup.spring.email.dto.EmailDtoRes;
import Growup.spring.member.converter.UserConverter;
import Growup.spring.member.model.User;
import Growup.spring.email.dto.EmailDtoReq;
import Growup.spring.email.service.EmailService;
import Growup.spring.security.JwtProvider;
import Growup.spring.member.service.UserServiceImpl;
import Growup.spring.member.dto.RefreshTokenRes;
import Growup.spring.member.dto.UserDtoReq;
import Growup.spring.member.dto.UserDtoRes;
import com.google.protobuf.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/users")
public class UserController {

    private final UserServiceImpl userService;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;

    /**
     * 24.01.19 작성자 : 정주현
     * 회원가입
    */
    @ResponseBody
    @PostMapping("")
    public ApiResponse<UserDtoRes.userRegisterRes> signUp(@RequestBody @Valid UserDtoReq.userRegisterReq request) {
        User user = userService.signUp(request);
        return ApiResponse.onSuccess(UserConverter.userDtoRes(user));
    }

    /**
     * 24.01.20 작성자 : 정주현
     * 이메일 인증요청
     */
    @ResponseBody
    @PostMapping("/auth")
    public ApiResponse<SuccessStatus> sendEmailAuth(@RequestBody @Valid EmailDtoReq.emailAuthReq request) {
        String text = "회원가입";
        userService.sendEmailAuth(request,text);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.19 작성자 : 정주현
     * 로그인
     */
    @ResponseBody
    @PostMapping("/login")
    public ApiResponse<UserDtoRes.userLoginRes> login(@RequestBody @Valid UserDtoReq.userLoginReq request) {
        return ApiResponse.onSuccess(userService.login(request));
    }
    /**
     * 24.01.19 작성자 : 정주현
     * AccessToken(만료) 재발급
     */
    @PostMapping("/token")
    public ApiResponse<RefreshTokenRes> login(@RequestHeader("Authorization") String refreshToken) {
        String accessToken = userService.inVaildToken(refreshToken);
       return ApiResponse.onSuccess(UserConverter.refreshTokenRes(accessToken));
    }

    /**
     * 24.01.20 작성자 : 정주현
     * 이메일 인증요청(비밀번호 재설정)
     */
    @ResponseBody
    @PostMapping("/password-auth")
    public ApiResponse<SuccessStatus> sendEmailAuthToPassword(@RequestBody @Valid EmailDtoReq.emailAuthReq request) {
        String text = "비밀번호 재설정";
        userService.sendEmailAuth(request,text);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.19 작성자 : 정주현
     * 비밀번호 재설정
     */
    @ResponseBody
    @PatchMapping("/password-restore")
    public ApiResponse<UserDtoRes.passwordRestoreRes> passwordRestore(@RequestBody @Valid UserDtoReq.passwordRestoreReq request){
        Long userId= jwtProvider.getUserID();
        User user = userService.pwRestore(request,userId);
        return ApiResponse.onSuccess(UserConverter.passwordRestoreRes(user));
    }

    /**
     * 24.01.20 작성자 : 정주현
     * 비밀번호 재설정 이메일 인증링크 확인(accessToken 발급)
     */
    @GetMapping("/email/password-verify")
    public ApiResponse<EmailDtoRes.emailAuthRes> authTokwn(@RequestParam(name = "certificationNumber") String certificationNumber,
                                                           @RequestParam(name = "email") String email){
        String accessToken = userService.passworAuthdToken(certificationNumber,email);
        return ApiResponse.onSuccess(EmailConverter.passwordAuthTokenRes(accessToken));
    }

    /**
     * 24.01.19 작성자 : 정주현
     * 이메일 인증링크 확인
     */
    @GetMapping("/email/verify")
    public ApiResponse<SuccessStatus> verifyCertificationNumber(@RequestParam(name = "certificationNumber") String certificationNumber,
                                                                @RequestParam(name = "email") String email) {
        userService.signUpAuth(certificationNumber,email);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.21 작성자 : 정주현
     * 로그아웃
     */
    @PostMapping("/logout")
    public ApiResponse<SuccessStatus> logout(@RequestHeader("Authorization") String accessToken){
        userService.logout(accessToken);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

}