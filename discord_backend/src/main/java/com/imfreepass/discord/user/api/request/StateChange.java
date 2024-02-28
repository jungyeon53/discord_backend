package com.imfreepass.discord.user.api.request;

import com.imfreepass.discord.user.entity.State;

import lombok.Getter;

@Getter
public class StateChange {
	
	private Long userId;
	private int stateId;
	
}
