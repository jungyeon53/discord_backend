package com.imfreepass.discord.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.friend.entity.Friend_Request;

@Repository
public interface FriendRepository extends JpaRepository<Friend_Request, Long>{
	
}
