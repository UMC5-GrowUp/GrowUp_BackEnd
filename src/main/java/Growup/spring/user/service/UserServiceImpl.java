package Growup.spring.user.service;

import Growup.spring.config.S3Uploader;
import Growup.spring.constant.handler.EmailHandler;
import Growup.spring.constant.handler.JwtHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.user.converter.UserConverter;
import Growup.spring.user.model.Enum.UserState;
import Growup.spring.email.service.EmailService;
import Growup.spring.user.repository.UserRepository;
import Growup.spring.security.JwtProvider;
import Growup.spring.security.RedisUtil;
import Growup.spring.user.dto.UserDtoReq;
import Growup.spring.user.model.User;
import Growup.spring.user.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final JwtProvider jwtProvider;
    public final RedisUtil redisUtil;
    public final EmailService emailService;
    public final S3Uploader s3Uploader;


    //회원가입
    @Override
    public User signUp(UserDtoReq.userRegisterReq request){

        // 닉네임 중복 확인
        checkNickDuplication(request.getNickName());

        // 이메일 형식 확인
        validateEmail(request.getEmail());

        // 이메일 중복 확인( 활동회원인지, 미인증 회원인지)
        if (userRepository.existsByEmailAndStatus(request.getEmail(),UserState.ACTIVE)||
                (userRepository.existsByEmailAndStatus(request.getEmail(),UserState.NONACTIVE))){
            throw new UserHandler(ErrorStatus.USER_EMAIL_DUPLICATE);
        }

        //비밀번호 정규화 확인
        validatePassword(request.getPassword());

        // 두 비밀번호 일치성 확인
        if(!request.getPassword().equals(request.getPasswordCheck())){
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NONEQULE);
        }
        //비밀번호 암호화
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        User newUser = UserConverter.toUser(request);

        return userRepository.save(newUser);
    }

    //회원가입 인증완료 확인
    @Override
    public void signUpAuth(String certificationNumber,String email){
        //인증이 유효한지
        User user = verifyEmail(certificationNumber, email);
        //인증완료시 활성화
        user.setStatus(UserState.valueOf("ACTIVE"));
    }

    //로그인
    @Override
    public UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request) {
        //해당 Email로 아이디 찾기 - 아이디 불일치
        User user = userRepository.findByEmailAndStatus(request.getEmail(), UserState.ACTIVE)
                .or(() -> userRepository.findByEmailAndStatus(request.getEmail(), UserState.NONACTIVE))
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_ID_PASSWORD_FOUND));

        // 인증 확인
        if(user.getStatus() == UserState.NONACTIVE){
            throw new EmailHandler(ErrorStatus._UNAUTHORIZED);
        }

        //비밀번호 불일치
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_ID_PASSWORD_FOUND);
        }

        //토큰 생성
        String accessToken = jwtProvider.createAccessToken(user.getId(),user.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // RefreshToken을 redis에 저장
        redisUtil.setDataExpire("RT:" + user.getId(), refreshToken, jwtProvider.REFRESH_TOKEN_VALID_TIME_IN_REDIS);


        return UserConverter.userLoginRes(user,accessToken,refreshToken);
    }

    // AccessToken 만료시 재발급
    @Override
    public String inVaildToken(String refreshToken) {;

        //pk 추출
        Long userId = jwtProvider.getUserPkInToken(refreshToken);
        //pk로 아이디 찾기
        User findUser = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //redis에서 userid를 기준으로 가져오기
        String refreshTokenInRedis = redisUtil.getData("RT:" + userId);

        //토큰이 일치하지 않을떄
        if(!refreshToken.equals(refreshTokenInRedis)) {
            throw new JwtHandler(ErrorStatus.JWT_REFRESHTOKEN_NOT_MATCH);
        }

        String newAccessToken = jwtProvider.createAccessToken(userId,findUser.getRole().name());

        return newAccessToken;
    }


    //비밀번호 재설정 인증완료 확인 후-accessToken 발급
    @Override
    public String passwordAuthToken(String certificationNumber,String email){
        User user = verifyEmail(certificationNumber, email);

        return jwtProvider.createAccessToken(user.getId(),String.valueOf(user.getRole()));
    }

    @Override
    //패스워드 재설정
    public User pwRestore(UserDtoReq.passwordRestoreReq request,Long userId){
        //유저 아이디 찾기(유저 객체 조회)
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //비밀번호 정규화 확인
        validatePassword(request.getPassword());
        //비밀번호 일치한지 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NONEQULE);
        }

        //기존 비밀번호와 일치한지 확인
        if(passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_EXIST);
        }

        //비밀번호 암호화 (저장)
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    //이메일 인증 전송
    @Override
    public void sendEmailAuth(String email,String text){

        User user = userRepository.findByEmailAndStatus(email,UserState.ACTIVE)
                .or(()->userRepository.findByEmailAndStatus(email,UserState.NONACTIVE))
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // ACTIVE 또는 NONACTIVE 상태인 경우에만 이메일 전송
        emailService.sendMail(email, text);

    }

    @Override
    //이메일 인증이 유효
    public User verifyEmail(String certificationNumber, String email) {

        User user = userRepository.findByEmailAndStatus(email, UserState.ACTIVE)
                .or(()->userRepository.findByEmailAndStatus(email, UserState.NONACTIVE))
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String authCode = redisUtil.getData("AuthCode_"+email);
        if(!certificationNumber.equals(authCode)){
            throw new EmailHandler(ErrorStatus.EMAIL_VERIFY_ERROR);
        }

        return user;
    }

    @Override
    //로그아웃
    public void logout(String accessToken) {
        Long userId = jwtProvider.getUserPkInToken(accessToken);

        Long expiration = jwtProvider.getExpiration(accessToken);

        redisUtil.deleteData("RT:" + userId);

        redisUtil.setDataExpire(accessToken,"logout",expiration/1000L);

    }

    @Override
    //이메일 변경
    public void emailChange(String email,Long userId){
        //이메일 유효성 검사
        validateEmail(email);

        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));

        user.setEmail(email);

        userRepository.save(user);
    }

    //현재비밀번호 같은지 체크후 인증번호 발송
    @Override
    public void currentPasswordCheckReq(Long userId, UserDtoReq.currentPasswordCheckReq request) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //비밀번호 정규화 확인
        validatePassword(request.getCurrentPwd());

        //기존 비밀번호와 일치한지 확인
        if (!passwordEncoder.matches(request.getCurrentPwd(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NONEQULE);
        }

    }

    //회원탈퇴
    @Override
    public void withdraw(Long userId, String currentPwd) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //기존 비밀번호와 일치한지 확인
        if (!passwordEncoder.matches(currentPwd, user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NONEQULE);
        }
        user.setStatus(UserState.WITHDRAW);

        userRepository.save(user);
    }

    //마이페이지(닉넴,이멜,이미지) 조회
    @Override
    public UserDtoRes.infoRes info(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));
        return UserConverter.info(user);
    }

    //프로필 이미지 변경
    @Override
    public User photoChange(MultipartFile photoImage, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String fileName = "";
        if (photoImage != null) {
            try {
                fileName = s3Uploader.upload(photoImage, "profileImage");
            } catch (IOException e) {
                throw new UserHandler(ErrorStatus.FILE_CHANGE_ERROR);
            }
            user.setPhotoUrl(fileName);
        }

        return userRepository.save(user);
    }

    //닉네임 변경
    @Override
    public void changeNickname(String nickName,Long userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //닉네임 중복확인
        checkNickDuplication(nickName);

        user.setNickName(nickName);

        userRepository.save(user);

    }

    @Override
    // 비밀번호 정규식 확인 함수
    public void validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_ERROR);
        }
    }
    @Override

    // 이메일 정규식 확인 함수
    public void validateEmail(String email){
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()){
            throw new UserHandler(ErrorStatus.USER_EMAIL_ERROR);
        }
    }
    @Override
    // 닉네임 중복 확인
    public void checkNickDuplication(String nickName){
        if (userRepository.existsByNickName(nickName)){
            throw new UserHandler(ErrorStatus.USER_NICKNAME_ERROR);
        }
    }


}
