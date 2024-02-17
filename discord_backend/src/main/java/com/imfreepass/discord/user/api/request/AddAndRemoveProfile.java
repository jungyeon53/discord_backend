package com.imfreepass.discord.user.api.request;

import com.imfreepass.discord.user.entity.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAndRemoveProfile {
	
	private Long userImgId;
	private User userId;
}
