package Growup.spring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

// 토큰을 생성하고 검증하는 클래스입니다.
// 해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    // 토큰 유효시간 30분
    public static final long TOKEN_VALID_TIME = 1000L * 60 * 5 * 144; // 5분(밀리초)
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 144; // 일주일(밀리초)
    public static final long REFRESH_TOKEN_VALID_TIME_IN_REDIS = 60 * 60 * 24 * 7; // 일주일 (초)

    private final JpaUserDetailsService jpaUserDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT access 토큰 생성
    public String createAccessToken(Long userPk, String roles) {
        return Jwts.builder()
                .setHeaderParam("type", "accessToken")
                .claim("userId",userPk) // 정보 저장
                .claim("roles",roles)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT refresh 토큰 생성
    public String createRefreshToken(Long userPk) {
        return Jwts.builder()
                .setHeaderParam("type", "refreshToken")
                .claim("userId",userPk) // 정보 저장
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    //토큰 파싱
    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }
    //토큰에서 userID(PK)추출
    public Long getUserPkInToken(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    // JWT 토큰에서 인증 정보(권한) 조회
    public Authentication getAuthentication(String token) {
        String userPk = String.valueOf(getUserPkInToken(token)); //long -> string으로 형변환

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(userPk);

       return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
    public String resolveAcceessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    //userID(PK) 추출
    public Long getUserID(){
        String token = resolveAcceessToken();
        return getUserPkInToken(token);
    }

    //토큰 유효성 검사
    public Claims validateToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    //redis에 저장할 남은 시간
    public Long getExpiration(String Token) {
        Date expiration = extractAllClaims(Token).getExpiration();
        long now = new Date().getTime();
        System.out.println(now);
        System.out.println(expiration.getTime());
        return expiration.getTime() - now;
    }
}