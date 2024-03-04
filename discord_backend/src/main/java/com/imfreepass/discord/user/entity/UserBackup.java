package com.imfreepass.discord.user.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="user_backup")
public class UserBackup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "backupId")
	private Long backupId;
	@Column(length = 50)
	private String email;
	@Column(length = 50)
	private String password;
	@Column(length = 50)
	private String nickname;
	@Column(length = 50)
	private String userHash;
	private String birth;
	private ZonedDateTime joinDate;
	@Column(name = "cancelDate")
	private ZonedDateTime cancelDate;
}
