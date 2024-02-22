package com.imfreepass.discord.friend.api.request;

import com.imfreepass.discord.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFriend {
	private Long friendRequestId;
	private User userId;
	private User sendUserId;
	private int friendState;
}
