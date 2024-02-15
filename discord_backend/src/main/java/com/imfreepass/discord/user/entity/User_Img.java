package com.imfreepass.discord.user.entity;

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

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_img")
public class User_Img {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_img_id; // img PK
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user_id;
	@Column(length = 50)
	private String original; // 원본 이름 
	@Column(length = 50)
	private String path; // 경로 
}
