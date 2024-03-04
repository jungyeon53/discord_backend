package com.imfreepass.discord.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewUserImg {

	private Long userImgId; // img PK
	private Long userId;
	private String original; // 원본 이름 
	private String path; // 경로 
}
