package Growup.spring.email.service;

import Growup.spring.constant.handler.EmailHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.security.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final RedisUtil redisUtil;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String email;


    public void sendMail(String email, String title){
        String authNum = getCertificationNumber();

        String DOMAIN_NAME ="http://localhost:8080";
        String content = String.format("%s/wego/users/email/verify?certificationNumber=%s&email=%s 링크를 3분 이내에 클릭해주세요.", DOMAIN_NAME, authNum, email);

        SimpleMailMessage emailForm = createEmailForm(email, title, content);

        mailSender.send(emailForm);
        redisUtil.setDataExpire("AuthCode_"+email, authNum,60*3);
    }


    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(title);
        mailMessage.setText(text);
        mailMessage.setFrom(email);
        return mailMessage;
    }

    //6자리 난수 생성
    public String getCertificationNumber() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder result = new StringBuilder();

            // 6자리의 인증번호 생성
            int digit = 0;
            for (int i = 0; i < 6; i++) {
                result.append(random.nextInt(10));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EmailHandler(ErrorStatus.NO_SUCH_ALGORITHM);
        }

    }
}
