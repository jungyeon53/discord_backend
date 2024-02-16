package com.imfreepass.discord.user.api.response;

import java.time.ZonedDateTime;

import com.imfreepass.discord.entity.State;

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
	private State stateId; // 상태pk 
	private String email;
	private String password;
	private String nickname;
	private String user_hash; // 사용자명 
	private String birth;
	private ZonedDateTime join_date; // 가입날짜 
}
