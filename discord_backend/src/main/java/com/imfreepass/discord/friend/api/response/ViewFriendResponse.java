package com.imfreepass.discord.friend.api.response;

import com.imfreepass.discord.user.api.response.ViewState;

import com.imfreepass.discord.user.api.response.ViewUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewFriendResponse {
	private Long friendRequestId; // 친구신청 pk 
	private Long fromUserId; // 받은 친구
	private ViewUser sendUserId; // 보낸친구
	private int friendState; // 친구 상태 (0) 친구아님 (1) 친구 (2) 차단 
}
