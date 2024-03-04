package com.imfreepass.discord.user.api.request;

import java.time.ZonedDateTime;

import com.imfreepass.discord.user.entity.User;
import lombok.AllArgsConstructor;
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

	private Long userId;
	private String email;
	private String password;
	private String nickname;
	private String userHash;
	private String birth;
	private User.State state;
	private User.State preState;
	private ZonedDateTime joinDate;
}
