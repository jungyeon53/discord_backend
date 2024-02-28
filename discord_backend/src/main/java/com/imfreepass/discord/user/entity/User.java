package com.imfreepass.discord.user.entity;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.imfreepass.discord.user.api.request.CreateUser;
import com.imfreepass.discord.user.api.response.ViewUser;
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
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId")
	private Long userId;
	
	private int stateId; // 상태pk
	@Column(name = "preState")
	private int preState; // 이전 상태 
	@Column(length = 50)
	private String email;
	@Column(length = 255)
	private String password;
	@Column(length = 50)
	private String nickname;
	@Column(length = 50, name = "userHash")
	private String userHash; // 사용자명 
	@Column(length = 50)
	private String birth;
	@Column(name = "joinDate")
	private ZonedDateTime joinDate; // 가입날짜 
	@Column(length = 500)
	private String refreshToken; // 리프레쉬 토큰


	@Autowired
	private static BCryptPasswordEncoder bCryptPasswordEncoder;
	public void registerUser() {
		this.password = bCryptPasswordEncoder.encode(this.password);
	}
	public static User registerUser(CreateUser user, BCryptPasswordEncoder bCryptPasswordEncoder){
        return User.builder()
                .email(user.getEmail())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .nickname(user.getNickname())
                .userHash(user.getUserHash())
                .birth(user.getBirth())
                .joinDate(ZonedDateTime.now())
                .stateId(4)
                .preState(1)
                .build();
    }


}