package com.imfreepass.discord.user.entity;

import com.imfreepass.discord.entity.Badge;

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
@Table(name="user_badge")
public class User_Badge {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_badge_id")
	private Long userBadgeId;
	@ManyToOne
	@JoinColumn(name = "user_id" , referencedColumnName = "user_id")
	private User userId;
	@ManyToOne
	@JoinColumn(name = "badge_id", referencedColumnName = "badge_id")
	private Badge badgeId;
}
