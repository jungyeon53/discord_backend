package com.imfreepass.discord.friend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>{
	
	long countByFromUserId(Long fromUserId);
	long countBySendUserId(Long sendUserId);
	List<Friend> findByFromUserIdOrSendUserId(Long fromUserId, Long sendUserId);

//	long countByFromUserIdOrSendUserIdAndStateId(Long fromUserId, Long sendUserId, List<Integer> stateIds);

}
