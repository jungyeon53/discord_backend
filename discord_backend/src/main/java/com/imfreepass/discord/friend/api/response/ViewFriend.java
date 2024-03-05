package com.imfreepass.discord.friend.api.response;

import com.imfreepass.discord.friend.entity.Friend;

import com.imfreepass.discord.user.api.response.ViewUser;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ViewFriend {
	private Long friendId;
	private ViewUser fromUserId;
	private ViewUser sendUserId;
	private Friend.FriendState friendState;
}
