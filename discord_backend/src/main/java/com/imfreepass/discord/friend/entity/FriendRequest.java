package com.imfreepass.discord.friend.entity;

import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="friend_request")
public class FriendRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "friendRequestId")
	private Long friendRequestId; // 친구신청 pk 
	
	private Long fromUserId; // 받은 친구
	
	private Long sendUserId; // 보낸친구
	@Column(name = "friendState")
	private int friendState; // 친구 상태 (0) 친구아님 (1) 친구 (2) 차단 

	public static FriendRequest FriendRequestInsert(SendFriendRequest request) {
		return FriendRequest.builder()
				.fromUserId(request.getFromUserId())
				.sendUserId(request.getSendUserId())
				.friendState(0)
				.build();
	}
}
