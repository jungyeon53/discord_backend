package com.imfreepass.discord.friend.api.response;

import com.imfreepass.discord.friend.entity.Friend;

import com.imfreepass.discord.user.api.response.ViewUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ViewFriend {
	private Long friendId;
	private ViewUser fromUserId;
	private ViewUser sendUserId;
	private int friendState;
}
