package com.imfreepass.discord.user.api.response;

import com.imfreepass.discord.user.entity.User;
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
	private User.State state;
	private String email;
	private String nickname;
	private String userHash;

	public static ViewUser from(User user) {
		return ViewUser.builder()
				.userId(user.getUserId())
				.state(user.getState())
				.email(user.getEmail())
				.nickname(user.getNickname())
				.userHash(user.getUserHash())
				.build();
	}
}
