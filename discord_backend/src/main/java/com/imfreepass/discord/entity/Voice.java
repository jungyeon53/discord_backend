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
@Table(name="voice")
public class Voice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long voice_id;
	@ManyToOne
	@JoinColumn(name = "voice_room_id", referencedColumnName = "voice_room_id")
	private Voice_Room voice_room_id;
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user_id;
	@Column(length = 50)
	private String title;
}
