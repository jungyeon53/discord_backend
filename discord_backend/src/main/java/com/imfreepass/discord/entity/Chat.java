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
@Table(name="chat")
public class Chat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chatId")
	private Long chatId;
	
	private Long chatRoomId;
	
	private Long fromUserId;
	private String text;
	private LocalDate date;
	private LocalTime time;
	@Column(name = "chatState")
	private int chatState;
}
