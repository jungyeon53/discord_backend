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
@Table(name="friend")
public class Friend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "friendId")
	private Long friendId;
	
	private Long fromUserId; // 받은 친구
	
	private Long sendUserId; // 보낸 친구
	@Column(name = "friendState")
	private int friendState; // 친구 상태 

    public static Friend FriendInsert(SendFriendRequest request) {
		return Friend.builder()
				.fromUserId(request.getFromUserId())
				.sendUserId(request.getSendUserId())
				.friendState(1)
				.build();
    }
}
