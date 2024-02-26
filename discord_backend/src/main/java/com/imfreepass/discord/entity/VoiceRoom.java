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
@Table(name="voice_room")
public class VoiceRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "voice_room_id")
	private Long voiceRoomId;
	@ManyToOne
	@JoinColumn(name = "server_id", referencedColumnName = "server_id")
	private Server serverId;
	@Column(length = 50, name = "voice_title")
	private String voiceTitle;
	@Column(name = "room_state")
	private int roomState;
}
