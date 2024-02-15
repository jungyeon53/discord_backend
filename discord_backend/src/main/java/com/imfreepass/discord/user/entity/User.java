package com.imfreepass.discord.user.entity;

import java.time.ZonedDateTime;

import com.imfreepass.discord.entity.State;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	@OneToOne
	@JoinColumn(name = "state_id" , referencedColumnName = "state_id")
	private State state_id; // 상태pk 
	@Column(length = 50)
	private String email;
	@Column(length = 255)
	private String password;
	@Column(length = 50)
	private String nickname;
	@Column(length = 50)
	private String user_hash; // 사용자명 
	@Column(length = 50)
	private String birth;
	private ZonedDateTime join_date; // 가입날짜 
	
	private String refreshToken; // 리프레쉬 토큰 
}