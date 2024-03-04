package com.imfreepass.discord.friend.api.response;

import com.imfreepass.discord.user.api.response.ViewUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ViewDistinctFriend {
    private Long friendId;
    private ViewUser fromUserId;
    private int friendState;
}
