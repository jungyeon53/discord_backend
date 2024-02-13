package com.imfreepass.discord.entity;

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
public class Block_Friend {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long block_id;
	
	@ManyToOne
	@JoinColumn(name = "send_user_id", referencedColumnName = "user_id")
	private User send_user_id; // 보낸 친구 
	@ManyToOne
	@JoinColumn(name = "from_user_id", referencedColumnName = "user_id")
	private User from_user_id; // 받은 친구 
	private int friend_state; // 친구 요청 상태 
}
