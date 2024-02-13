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
public class Voice_Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long voice_room_id;
	@ManyToOne
	@JoinColumn(name = "server_id", referencedColumnName = "server_id")
	private Server server_id;
	@Column(length = 50)
	private String voice_title;
	private int room_state;
}
