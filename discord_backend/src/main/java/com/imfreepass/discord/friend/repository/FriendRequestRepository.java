package com.imfreepass.discord.friend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.friend.api.request.AddFriend;
import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.friend.entity.Friend_Request;
import com.imfreepass.discord.user.entity.User;

@Repository
public interface FriendRequestRepository extends JpaRepository<Friend_Request, Long>{

	List<Friend_Request> findByUserId(User userId);
	long countByUserId(User userId);
	
	@Query("SELECT u FROM Friend_Request u WHERE u.userId = :userId AND u.sendUserId = :sendUserId")
	Optional<Friend_Request> findByUserIdAndSendUserId(@Param("userId") User userId, @Param("sendUserId") User sendUserId);

}
