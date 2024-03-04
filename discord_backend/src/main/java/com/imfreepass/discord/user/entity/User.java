package com.imfreepass.discord.user.entity;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.imfreepass.discord.user.api.request.CreateUser;
import com.imfreepass.discord.user.api.response.ViewUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
	@Enumerated(EnumType.STRING)
	private State state;
	@Column(name = "preState")
	@Enumerated(EnumType.STRING)
	private State preState;
	@Column(length = 50)
	private String email;
	@Column(length = 255)
	private String password;
	@Column(length = 50)
	private String nickname;
	@Column(length = 50, name = "userHash")
	private String userHash;
	@Column(length = 50)
	private String birth;
	@Column(name = "joinDate")
	private ZonedDateTime joinDate;
	@Column(length = 500)
	private String refreshToken;

	public enum State{
		ONLINE, IDLE, DO_NOT_DISTURB, INVISIBLE
	}

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
                .state(State.IDLE)
                .preState(State.IDLE)
                .build();
    }
}