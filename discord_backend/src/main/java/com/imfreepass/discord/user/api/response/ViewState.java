package com.imfreepass.discord.user.api.response;

import java.time.ZonedDateTime;

import com.imfreepass.discord.user.entity.State;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewState {

	private Long stateId;
	private String stateName;
	private String stateText;
}
