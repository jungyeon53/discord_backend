package com.imfreepass.discord.user.entity;


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

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_img")
public class User_Img {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_img_id")
	private Long userImgId; // img PK
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User userId;
	@Column(length = 50)
	private String original; // 원본 이름 
	@Column(length = 1000)
	private String path; // 경로 
}
