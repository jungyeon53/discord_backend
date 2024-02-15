package com.imfreepass.discord.user.api.response;

import lombok.Getter;

@Getter
public class LoginResponse {
	
	private String email;
	private String accessToken;
	private String refreshToken;
	private String message;
	
	public LoginResponse(String email, String accessToken, String refreshToken, String message) {
		this.email = email;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.message = message;
	}
	public LoginResponse(String email, String accessToken, String message) {
		this.email = email;
		this.accessToken = accessToken;
		this.message = message;
	}
}
