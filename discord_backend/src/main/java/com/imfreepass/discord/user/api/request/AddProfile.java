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
public class AddProfile {
	
	private Long user_id;
	private String original; // 원본 이름 
	private String path; // 경로 
}
