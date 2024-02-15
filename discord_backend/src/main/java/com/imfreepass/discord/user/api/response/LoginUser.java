package com.imfreepass.discord.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
	
	private Long user_id;
	private String email;
	private String password;
	private String nickname;
	private String message;
	private String acessToken;
	private String refreshToken;
	
	
}
