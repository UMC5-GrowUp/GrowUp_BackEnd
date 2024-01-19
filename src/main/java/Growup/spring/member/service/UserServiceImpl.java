package Growup.spring.member.service;

import Growup.spring.constant.handler.EmailHandler;
import Growup.spring.constant.handler.JwtHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.member.converter.UserConverter;
import Growup.spring.member.model.Enum.UserState;
import Growup.spring.email.dto.EmailDtoReq;
import Growup.spring.email.service.EmailService;
import Growup.spring.member.repository.UserRepository;
import Growup.spring.security.JwtProvider;
import Growup.spring.security.RedisUtil;
import Growup.spring.member.dto.UserDtoReq;
import Growup.spring.member.model.User;
import Growup.spring.member.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public User signUp(UserDtoReq.userRegisterReq request){

        // 이메일 형식 확인
        validateEmail(request.getEmail());

        // 닉네임 중복 확인
        if (userRepository.existsByNickName(request.getNickName())){
            throw new UserHandler(ErrorStatus.USER_NICKNAME_ERROR);
        }

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserHandler(ErrorStatus.USER_EMAIL_DUPLICATE);
        }

        //비밀번호 정규화 확인
        validatePassword(request.getPassword());
        //비밀번호 암호화
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        User newUser = UserConverter.toUser(request);

        return userRepository.save(newUser);
    }

    @Override
    public UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request) {
        //해당 Email로 아이디 찾기 - 아이디 불일치
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_ID_PASSWORD_FOUND));

        // 인증 확인
        if(user.getStatus() == UserState.valueOf("NONACTIVE")){
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

    //비밀번호 찾기 - 인증번호 요청

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
        userRepository.save(user);
        return user;
    }

    //이메일 인증 전송
    @Override
    public void sendEmailAuth(EmailDtoReq.emailAuthReq request){

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        String title = "이메일 인증안내";

        emailService.sendMail(request.getEmail(),title);
    }
    @Override
    //이메일 인증이 유효한지 검사
    public void verifyEmail(String certificationNumber, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String authCode = redisUtil.getData("AuthCode_"+email);
        if(!certificationNumber.equals(authCode)){
            throw new EmailHandler(ErrorStatus.EMAIL_VERIFY_ERROR);
        }
        user.setStatus(UserState.valueOf("ACTIVE"));
    }

    // 비밀번호 정규식 확인 함수
    private void validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_ERROR);
        }
    }

    // 이메일 정규식 확인 함수
    private void validateEmail(String email){
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()){
            throw new UserHandler(ErrorStatus.USER_EMAIL_ERROR);
        }
    }
}
