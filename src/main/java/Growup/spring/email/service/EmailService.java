package Growup.spring.email.service;

import Growup.spring.constant.handler.EmailHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.security.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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


    public void sendMail(String email, String text){
        String authNum = getCertificationNumber();

        String title = "이메일 인증안내";
        String DOMAIN_NAME = "https://dev.jojoumc.shop";

        String url;
        if (text.equals("비밀번호 재설정")){
            url = "password-verify";
        }else
            url = "verify";

        //html 형식
        String content = generateEmailContent(text, authNum, email, DOMAIN_NAME, url);
        MimeMessage emailForm = createEmailForm(email, title, content);

        mailSender.send(emailForm);
        redisUtil.setDataExpire("AuthCode_"+email, authNum,60*3);
    }


    private MimeMessage createEmailForm(String toEmail, String title, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(content, true);  // HTML 형식으로 설정
            helper.setFrom(email);
            return mimeMessage;
        }catch (MessagingException e){
            throw new EmailHandler(ErrorStatus.EMAIL_SEND_ERROR);
        }
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

    //html 형식
    private String generateEmailContent(String text, String authNum, String email, String domainName,String  url) {
        return String.format(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "</head>" +
                        "<body>" +
                        " <div" +
                        "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                        "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">GROWUP</span><br />" +
                        "		<span style=\"color: #02b875\">메일인증</span> 안내입니다." +
                        "	</h1>\n" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        "		%s 님 안녕하세요.<br />" +
                        "		GROWUP에 가입해 주셔서 진심으로 감사드립니다.<br />" +
                        "		아래 <b style=\"color: #02b875\">'메일 인증'</b> 버튼을 클릭하여 %s을 완료해 주세요.<br />" +
                        "		감사합니다." +
                        "	</p>" +
                        "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                        "	href=\"%s/growup/users/email/%s?certificationNumber=%s&email=%s\" target=\"_blank\">" +
                        "		<p" +
                        "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                        "			메일 인증</p>" +
                        "	</a>" +
                        "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                        " </div>" +
                        "</body>" +
                        "</html>",
                email,text, domainName, url, authNum, email
        );
    }
}
