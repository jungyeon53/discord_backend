package com.imfreepass.discord.friend.api.request;

import com.imfreepass.discord.user.api.response.ViewUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlockUser {

    private Long blockId;
    private ViewUser fromUserId;
    private ViewUser sendUserId;
    private int friendState;
}
