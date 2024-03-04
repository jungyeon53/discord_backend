package com.imfreepass.discord.user.api.request;

import com.imfreepass.discord.user.entity.User;
import lombok.Getter;

@Getter
public class StateChange {
	
	private Long userId;
	private User.State state;
	
}
