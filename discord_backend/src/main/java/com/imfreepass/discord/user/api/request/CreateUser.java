package com.imfreepass.discord.user.api.request;

import java.time.ZonedDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.imfreepass.discord.user.entity.State;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원가입 
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {
	
	private String email;
	private String password;
	private String nickname; // 별명 
	private String user_hash; // 사용자명 
	private String birth;
	private State stateId;
	private int preState;
	private ZonedDateTime joinDate;
	
//	public CreateUser encodePw(PasswordEncoder encoder) {
//		this.password = encoder.encode(password);
//		return this;
//	}
}
