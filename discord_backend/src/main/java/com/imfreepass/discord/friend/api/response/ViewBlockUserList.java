package com.imfreepass.discord.friend.api.response;

import com.imfreepass.discord.friend.entity.BlockFriend;
import com.imfreepass.discord.friend.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewBlockUserList {
    private Long blockId;
    private Long sendUserId;
    private Long fromUserId;
    private Friend.FriendState friendState;

    public static ViewBlockUserList from(BlockFriend user) {
        return ViewBlockUserList.builder()
                .blockId(user.getBlockId())
                .sendUserId(user.getSendUserId())
                .fromUserId(user.getFromUserId())
                .friendState(user.getFriendState())
                .build();
    }
}
