package com.imfreepass.discord.friend.api.request;

import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.user.api.response.ViewUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlockUser {


    private Long fromUserId;
    private Long sendUserId;
    private Friend.FriendState friendState;
}
