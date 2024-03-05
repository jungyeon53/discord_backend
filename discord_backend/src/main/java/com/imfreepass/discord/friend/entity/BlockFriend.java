package com.imfreepass.discord.friend.entity;

import com.imfreepass.discord.friend.api.request.BlockUser;
import com.imfreepass.discord.friend.api.response.ViewBlockUser;
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
@Table(name="blockFriend")
public class BlockFriend {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blockId")
	private Long blockId;
	private Long sendUserId;
	private Long fromUserId;
	@Column(name = "friendState")
	private Friend.FriendState friendState;

	public static BlockFriend BlockFriendInsert(ViewBlockUser request){
		return BlockFriend.builder()
				.fromUserId(request.getFromUserId())
				.sendUserId(request.getSendUserId())
				.friendState(Friend.FriendState.BLOCK)
				.build();
	}
}
