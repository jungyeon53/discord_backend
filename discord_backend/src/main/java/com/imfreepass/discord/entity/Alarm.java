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
@Table(name="alarm")
public class Alarm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alarm_id")
	private Long alarmId;
	@ManyToOne
	@JoinColumn(name = "chat_id", referencedColumnName = "chat_id")
	private Chat chatId; // 채팅 pk 
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName = "user_id")
	private User userId; // 받은 사람 
	@Column(name = "chat_state")
	private int chatState; // 채팅 확인 여부 dd 
}
