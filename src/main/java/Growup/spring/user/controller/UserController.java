package Growup.spring.user.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.email.converter.EmailConverter;
import Growup.spring.email.dto.EmailDtoRes;
import Growup.spring.user.converter.UserConverter;
import Growup.spring.user.model.User;
import Growup.spring.email.dto.EmailDtoReq;
import Growup.spring.security.JwtProvider;
import Growup.spring.user.service.UserService;
import Growup.spring.user.dto.RefreshTokenRes;
import Growup.spring.user.dto.UserDtoReq;
import Growup.spring.user.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    /**
     * 24.01.19 작성자 : 정주현
     * 회원가입
    */
    @PostMapping("/signup")
    public ApiResponse<UserDtoRes.userRegisterRes> signUp(@RequestBody @Valid UserDtoReq.userRegisterReq request) {
        User user = userService.signUp(request);
        return ApiResponse.onSuccess(UserConverter.userDtoRes(user));
    }

    /**
     * 24.01.20 작성자 : 정주현
     * 이메일 인증요청
     */
    @PostMapping("/auth")
    public ApiResponse<SuccessStatus> sendEmailAuth(@RequestBody @Valid EmailDtoReq.emailAuthReq request) {
        String text = "인증";
        userService.sendEmailAuth(request.getEmail(),text);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.19 작성자 : 정주현
     * 로그인
     */
    @PostMapping("/login")
    public ApiResponse<UserDtoRes.userLoginRes> login(@RequestBody @Valid UserDtoReq.userLoginReq request) {
        return ApiResponse.onSuccess(userService.login(request));
    }

    /**
     * 24.01.19 작성자 : 정주현
     * AccessToken(만료) 재발급
     */
    @PostMapping("/token-reissue")
    public ApiResponse<RefreshTokenRes> reissue(@RequestHeader("Authorization") String refreshToken) {
        String accessToken = userService.inVaildToken(refreshToken);
       return ApiResponse.onSuccess(UserConverter.refreshTokenRes(accessToken));
    }

    /**
     * 24.01.20 작성자 : 정주현
     * 이메일 인증요청(비밀번호 재설정)(이메일로)(회원가입시)
     */
    @PostMapping("/password-auth")
    public ApiResponse<SuccessStatus> sendEmailAuthToPassword(@RequestBody @Valid EmailDtoReq.emailAuthReq request) {
        String text = "비밀번호 재설정";
        userService.sendEmailAuth(request.getEmail(),text);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.19 작성자 : 정주현
     * 비밀번호 재설정(회원가입시)
     */
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
    public ApiResponse<EmailDtoRes.emailAuthRes> authToken(@RequestParam(name = "certificationNumber") String certificationNumber,
                                                           @RequestParam(name = "email") String email){
        String accessToken = userService.passwordAuthToken(certificationNumber,email);
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

    /**
     * 24.01.31 작성자 : 정주현
     * 닉네임 중복 확인
     */
    @PostMapping("/exist-nickname")
    public ApiResponse<SuccessStatus> checkNickDuplication(@RequestBody @Valid UserDtoReq.nicknameDuplicationReq request){
        userService.checkNickDuplication(request.getNickName());
        System.out.println("nickName:"+request.getNickName());
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }


    /**
     * 24.01.21 작성자 : 정주현
     * 닉네임 변경
     */
    @PatchMapping("/nickname-change")
    public ApiResponse<SuccessStatus> changeNickname(@RequestBody @Valid UserDtoReq.nicknameDuplicationReq request){
        Long userId = jwtProvider.getUserID();
        userService.changeNickname(request.getNickName(),userId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }


    /**
     * 24.01.22 작성자 : 정주현
     * 이메일 변경
     */
    @PatchMapping("/email-change")
    public ApiResponse<SuccessStatus> emailChange(@RequestBody @Valid EmailDtoReq.emailChangeReq request){
        Long userId = jwtProvider.getUserID();
        userService.emailChange(request.getEmail(),userId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.22 작성자 : 정주현
     * 현재 비밀번호 확인 후 - 인증 발송
     */
    @PostMapping("/password-check")
    public ApiResponse<SuccessStatus> currentPasswordCheckReq(@RequestBody @Valid UserDtoReq.currentPasswordCheckReq request){
        Long userId = jwtProvider.getUserID();
        userService.currentPasswordCheckReq(userId,request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.01.22 작성자 : 정주현
     * 내 정보 조회
     */
    @GetMapping("/info")
    public ApiResponse<UserDtoRes.infoRes> info(@RequestHeader("Authorization") String accessToken){
        Long userId = jwtProvider.getUserPkInToken(accessToken);
        return ApiResponse.onSuccess(userService.info(userId));
    }

    /**
     * 24.01.22 작성자 : 정주현
     * 프로필 사진 변경
     */
    @PatchMapping("/photo-change")
    public ApiResponse<UserDtoRes.photoChangeRes> photoChange(@RequestPart @Valid MultipartFile photoImage){
        Long userId = jwtProvider.getUserID();
        User user =userService.photoChange(photoImage,userId);
        return ApiResponse.onSuccess(UserConverter.photoChangeRes(user));
    }

    /**
     * 24.01.22 작성자 : 정주현
     * 회원탈퇴
     */
    @PatchMapping("/withdraw")
    public ApiResponse<SuccessStatus> withdraw(@RequestBody @Valid UserDtoReq.withdrawReq request){
        Long userId = jwtProvider.getUserID();
        userService.withdraw(userId,request.getCurrentPwd());
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }
}

