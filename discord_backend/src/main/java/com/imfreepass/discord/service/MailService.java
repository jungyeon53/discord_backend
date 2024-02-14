package com.imfreepass.discord.service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import com.imfreepass.discord.config.jwt.TokenProvider;
import com.imfreepass.discord.entity.User;
import com.imfreepass.discord.repository.UserRepository;

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
	
	public void createLink(String email, Long user_id) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        Duration tokenTime = Duration.ofMinutes(30);
	        user_id = user.getUser_id();
	        tokenLink = provider.makeTokenLink(user_id, email, tokenTime);
	    } else {
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "가입되지 않은 사용자입니다");
	    }
		
	}
	
	public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException{
		// 토큰 값 
		createLink(email, null);
		String setFrom = "discordcteam@gmail.com";
		String title = "Discord Clone 비밀번호 재설정 요청";
		
		MimeMessage message = mailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, email);
		message.setSubject(title);
		message.setFrom(setFrom);
		message.setText(setContext(tokenLink), "utf-8", "html");
		
		return message;
	}
	public String setContext(String link) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process("mail", context);
    }
	public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailForm = createEmailForm(toEmail);
        mailSender.send(mailForm);

        return tokenLink;
    }
}
