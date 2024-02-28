package com.imfreepass.discord.user.api.response;

import java.time.ZonedDateTime;

import com.imfreepass.discord.user.entity.State;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewUser {

	private Long userId;
	private int stateId;
	private String email;
//	private String password;
	private String nickname;
	private String user_hash;
//	private String birth;
//	private ZonedDateTime join_date;
//	private String refreshToken;
}
