package com.imfreepass.discord.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name="chat_reply")
public class Chat_reply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chat_reply_id;
	@ManyToOne
	@JoinColumn(name = "chat_id", referencedColumnName = "chat_id")
	private Chat chat_id;
	@ManyToOne
	@JoinColumn(name = "send_user_id", referencedColumnName = "user_id")
	private User send_user_id; // 보낸사람 
	private String content; // 내용 
	private LocalDate date; // 날짜 
	private LocalTime time; // 시간 
	private int chat_state; // 채팅 고정 여부 
}
