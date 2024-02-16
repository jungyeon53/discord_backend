package com.imfreepass.discord.user.api.request;

import java.time.ZonedDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {
	
	private String email;
	private String password;
	private String nickname; // 별명 
	private String user_hash; // 사용자명 
	private String birth;
	private ZonedDateTime joinDate;
	
//	public CreateUser encodePw(PasswordEncoder encoder) {
//		this.password = encoder.encode(password);
//		return this;
//	}
}
