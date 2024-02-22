package com.imfreepass.discord.friend.api.response;

import com.imfreepass.discord.user.entity.User;

import lombok.Getter;


@Getter
public class ViewFriend {
	private Long friendId;
	private User userId; // 받은 친구 
	private User sendUserId; // 보낸 친구
	private int friendState; // 친구 상태 
}
