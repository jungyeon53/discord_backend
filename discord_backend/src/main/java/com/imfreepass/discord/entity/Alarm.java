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
	@Column(name = "alarmId")
	private Long alarmId;
	private Long chatId; // 채팅 pk 
	private Long userId; // 받은 사람 
	@Column(name = "chat_state")
	private int chatState; // 채팅 확인 여부 dd 
}
