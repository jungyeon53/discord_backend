package com.imfreepass.discord.friend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.friend.api.response.ViewFriendResponse;
import com.imfreepass.discord.friend.entity.Friend_Request;
import com.imfreepass.discord.friend.repository.FriendRepository;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.repository.UserRepository;
import com.imfreepass.discord.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
	
	private final FriendRepository friendRepository;
	private final UserRepository userRepository;
	
	/**
	 * 친구신청
	 * @param req
	 * @return
	 */
	public Friend_Request sendFriendRequest(SendFriendRequest req) {
		// 받은 사람 
		Optional<User> userId =  userRepository.findById(req.getUserId().getUserId());
		// 보낸 사람 
		Optional<User> sendId = userRepository.findById(req.getSendUserId().getUserId());
		User user = userId.orElseThrow(() -> new RuntimeException("친구신청 받은 유저를 찾을 수 없습니다")); 
	    User sendUser = sendId.orElseThrow(() -> new RuntimeException("친구신청 보낸 유저를 찾을 수 없습니다"));
	    
	    Friend_Request friend = Friend_Request.builder()
	            .userId(user)
	            .sendUserId(sendUser)
	            .friendState(0)
	            .build();
	    friendRepository.save(friend);
	    return friend;
	}
	
	public Optional<Friend_Request> getFriendRequests(ViewFriendResponse res){
		return friendRepository.findById(res.getUserId().getUserId());
	}
	
	
}
