package com.imfreepass.discord.user.api.request;

import com.imfreepass.discord.user.entity.State;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddState {
	
	private State stateId;
	private String stateName;
	private String stateText;
	
}
