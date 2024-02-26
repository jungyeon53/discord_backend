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
	@Column(name = "backup_id")
	private Long backupId;
	@Column(length = 50)
	private String email;
	@Column(length = 50)
	private String password;
	@Column(length = 50)
	private String nickname;
	@Column(length = 50)
	private String user_hash; // 사용자명 
	@Column(length = 50)
	private String birth;
	private ZonedDateTime join_date; // 가입날짜 
	@Column(name = "cancel_date")
	private ZonedDateTime cancelDate; // 탈퇴 날짜 
}