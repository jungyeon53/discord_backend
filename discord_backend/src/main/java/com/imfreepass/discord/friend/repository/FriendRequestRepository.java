package com.imfreepass.discord.friend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.friend.api.request.AddFriend;
import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.friend.entity.FriendRequest;
import com.imfreepass.discord.user.entity.User;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long>{

//	List<FriendRequest> findByUserId(Long userId);
//	long countByUserId(Long userId);
	Optional<FriendRequest> findByFromUserIdAndSendUserId(Long fromUserId, Long sendUserId);
	boolean existsByFromUserId(Long sendUserId);

}
