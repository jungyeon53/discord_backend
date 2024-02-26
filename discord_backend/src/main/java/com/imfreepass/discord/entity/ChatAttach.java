package com.imfreepass.discord.entity;

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
@Table(name="chat_attach")
public class ChatAttach {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_img_id")
	private Long chatImgId;
	@ManyToOne
	@JoinColumn(name = "chat_id", referencedColumnName = "chat_id")
	private Chat chatId;
	@Column(length = 50)
	private String original;
	@Column(length = 50)
	private String path;
	
}