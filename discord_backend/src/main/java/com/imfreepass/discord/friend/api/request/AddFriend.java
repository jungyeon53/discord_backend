package com.imfreepass.discord.friend.api.request;

import com.imfreepass.discord.friend.entity.Friend;
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
	private Long userId;
	private Long sendUserId;
	private Friend.FriendState friendState;
}
