package com.imfreepass.discord.entity;

import com.imfreepass.discord.user.entity.User;

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
public class Friend_Request {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long friend_request_id; // 친구신청 pk 
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user_id; // 받은 친구 
	@ManyToOne
	@JoinColumn(name = "send_user_id", referencedColumnName = "user_id")
	private User send_user_id; // 보낸친구 
	private int friend_state; // 친구 상태 
}
