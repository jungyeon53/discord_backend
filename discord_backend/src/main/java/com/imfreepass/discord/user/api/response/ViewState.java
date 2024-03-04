package com.imfreepass.discord.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewState {

	private int stateId;
	private String stateName;
	private String stateText;
}
