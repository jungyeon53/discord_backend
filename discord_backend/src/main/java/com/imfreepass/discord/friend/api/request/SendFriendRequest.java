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
public class SendFriendRequest {
	private Long friendRequestId; // 친구신청 pk 
	private User userId; // 받은 친구 
	private User sendUserId; // 보낸친구
	private int friendState; // 친구 상태 (0) 친구아님 (1) 친구 (2) 차단 
}
