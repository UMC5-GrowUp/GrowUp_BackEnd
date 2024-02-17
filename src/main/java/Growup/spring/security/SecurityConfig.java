package Growup.spring.security;

import Growup.spring.security.handler.CustomAccessDeniedHandler;
import Growup.spring.security.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(new ArrayList<String>() {{
                add("http://localhost:3000");
                add("https://growup-umc5th.netlify.app");
            }});
            config.setAllowCredentials(true);
            return config;
        };
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable();
        http.cors().configurationSource(corsConfigurationSource()); // CORS 설정 적용

        http.httpBasic().disable()

                .authorizeRequests()// 요청에 대한 사용권한 체크

                .antMatchers("/growup/users/password-restore").hasRole("USER")
                .antMatchers("/growup/users/token").hasAnyRole("USER", "ADMIN")
                .antMatchers("/growup/users/logout").hasAnyRole("USER", "ADMIN")
                .antMatchers("/growup/users/token-reissue").hasRole("USER")
                .antMatchers("/growup/users/**").permitAll()
                .antMatchers("/growup/users/admin/**").hasRole("ADMIN")
                .antMatchers("/growup/upperAbleNoToken").permitAll()
                .antMatchers("/growup/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()

                .and()

                .httpBasic(Customizer.withDefaults())
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())//인가 핸들링(권한)
                .authenticationEntryPoint(authenticationEntryPoint())

                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationFilter.class); //ExceptionHandlerFilter필터를 앞에둬서 필터에서 예외처리

        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtProvider,redisUtil);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean // 추가 빈으로 등록
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter(){return new ExceptionHandlerFilter();}

    //BCcryt 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

