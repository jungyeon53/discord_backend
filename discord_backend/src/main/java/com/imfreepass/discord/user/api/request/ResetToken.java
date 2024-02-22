package com.imfreepass.discord.user.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetToken {
	private Long userId;
	private String email;
	private String password;
	private String nickname;
	private String message;
	private String acessToken;
	private String refreshToken;
}
