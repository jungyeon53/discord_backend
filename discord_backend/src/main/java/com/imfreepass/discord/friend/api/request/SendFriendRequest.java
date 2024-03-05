package com.imfreepass.discord.friend.api.request;

import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.user.entity.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendFriendRequest {

	private Long fromUserId; // 받은 친구
	private Long sendUserId; // 보낸친구
	@ApiModelProperty(hidden = true)
	private Friend.FriendState friendState;
}
