package com.imfreepass.discord.entity;

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
@Table(name="state")
public class Server_User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chat_room_user_id;
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user_id;
	@ManyToOne
	@JoinColumn(name = "server_id", referencedColumnName = "server_id")
	private Server server_id;
}
