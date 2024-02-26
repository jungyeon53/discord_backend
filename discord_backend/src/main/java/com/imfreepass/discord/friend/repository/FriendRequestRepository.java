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

	List<FriendRequest> findByUserId(User userId);
	long countByUserId(User userId);
	Optional<FriendRequest> findByUserIdAndSendUserId(User userId, User sendUserId);


}
