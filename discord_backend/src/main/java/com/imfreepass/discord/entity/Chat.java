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
@Table(name="chat")
public class Chat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chat_id;
	@ManyToOne
	@JoinColumn(name = "chat_room_id" , referencedColumnName = "chat_room_id")
	private Chat_Room chat_room_id;
	@ManyToOne
	@JoinColumn(name = "from_user_id", referencedColumnName = "user_id")
	private User from_user_id;

	private String text;
	private LocalDate date;
	private LocalTime time;
	private int chat_state;
}
