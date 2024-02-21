package com.imfreepass.discord.user.entity;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@Column(name = "user_id")
	private Long userId;
	@ManyToOne
	@JoinColumn(name = "state_id" , referencedColumnName = "state_id")
	private State stateId; // 상태pk
	@Column(name = "pre_state")
	@Schema(hidden = true)
	private int preState; // 이전 상태 
	@Column(length = 50)
	private String email;
	@Column(length = 255)
	@JsonIgnore
	private String password;
	@Column(length = 50)
	private String nickname;
	@Column(length = 50, name = "user_hash")
	private String userHash; // 사용자명 
	@Column(length = 50)
	@Schema(hidden = true)
	private String birth;
	@Column(name = "join_date")
	@Schema(hidden = true)
	private ZonedDateTime joinDate; // 가입날짜 
	@Column(length = 500)
	@JsonIgnore
	private String refreshToken; // 리프레쉬 토큰 
}