package com.imfreepass.discord.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.user.entity.User;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>{
	
	long countByUserId(User userId);
	long countBySendUserId(User sendUserId);
}
