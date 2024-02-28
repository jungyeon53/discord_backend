package com.imfreepass.discord.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name="chat_reply")
public class ChatReply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chatReplyId")
	private Long chatReplyId;
	
	private Long chatId;
	
	private Long sendUserId; // 보낸사람 
	private String content; // 내용 
	private LocalDate date; // 날짜 
	private LocalTime time; // 시간 
	@Column(name = "chatState")
	private int chatState; // 채팅 고정 여부 
}
