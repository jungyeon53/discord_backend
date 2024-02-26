package com.imfreepass.discord.entity;

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
@Table(name="block_friend")
public class BlockFriend {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "block_id")
	private Long blockId;
	
	@ManyToOne
	@JoinColumn(name = "send_user_id", referencedColumnName = "user_id")
	private User sendUserId; // 보낸 친구 
	@ManyToOne
	@JoinColumn(name = "from_user_id", referencedColumnName = "user_id")
	private User fromUserId; // 받은 친구 
	@Column(name = "friend_state")
	private int friendState; // 친구 요청 상태 
}