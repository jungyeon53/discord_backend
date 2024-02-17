package com.imfreepass.discord.user.service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import com.imfreepass.discord.user.config.jwt.TokenProvider;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.repository.UserRepository;

import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	
	private final JavaMailSender mailSender;
	private final ISpringTemplateEngine templateEngine;
	private final TokenProvider provider;
	private final UserRepository userRepository;
	private String tokenLink;
	
	/**
	 * 이메일 url에 토큰 추가 
	 * @param email
	 * @param user_id
	 */
	public void createLink(String email, Long user_id) {
	        Duration tokenTime = Duration.ofMinutes(30);
	        tokenLink = provider.makeTokenLink(user_id, email, tokenTime);
	}
	
	/**
	 * 이메일 폼 생성 
	 * @param email
	 * @param user_id
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public MimeMessage createEmailForm(String email, Long user_id) throws MessagingException, UnsupportedEncodingException{
		// 토큰 값 
		createLink(email, user_id);
		String setFrom = "discordcteam@gmail.com";
		String title = "Discord Clone 비밀번호 재설정 요청";
		
		MimeMessage message = mailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, email);
		message.setSubject(title);
		message.setFrom(setFrom);
		message.setText(setContext(tokenLink), "utf-8", "html");
		
		return message;
	}
	/**
	 * 템플릿 + link 처리 
	 * @param link
	 * @return
	 */
	public String setContext(String link) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process("mail", context);
    }
	/**
	 * 이메일 전송 
	 * @param toEmail
	 * @param user_id
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public String sendEmail(String toEmail, Long user_id) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailForm = createEmailForm(toEmail, user_id);
        mailSender.send(mailForm);

        return tokenLink;
    }
}
