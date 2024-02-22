package com.imfreepass.discord.friend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.imfreepass.discord.friend.api.request.AddFriend;
import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.friend.api.response.ViewFriendResponse;
import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.friend.entity.Friend_Request;
import com.imfreepass.discord.friend.repository.FriendRepository;
import com.imfreepass.discord.friend.repository.FriendRequestRepository;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.repository.UserRepository;
import com.imfreepass.discord.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class FriendService {
	
	private final FriendRequestRepository friendRequestRepository;
	private final UserRepository userRepository;
	private final FriendRepository friendRepository;
	
	/**
	 * 친구신청
	 * @param req
	 * @return
	 */
	public Friend_Request sendFriendRequest(SendFriendRequest req) {
		// 받은 사람 
		Optional<User> userId =  userRepository.findById(req.getUserId().getUserId());
		// 보낸 사람 > 나 
		Optional<User> sendId = userRepository.findById(req.getSendUserId().getUserId());
		User user = userId.orElseThrow(() -> new RuntimeException("친구신청 받은 유저를 찾을 수 없습니다")); 
	    User sendUser = sendId.orElseThrow(() -> new RuntimeException("친구신청 보낸 유저를 찾을 수 없습니다"));
	    // 내가 보내려는 친구한테 친구 요청을 받았는지 체크 && 같은 친구한테 보내는지 체크 
	    List<Friend_Request> me = friendRequestRepository.findByUserId(sendUser);
	    Optional<Friend_Request> userIs = friendRequestRepository.findByUserIdAndSendUserId(user, sendUser);
	    if (me.isEmpty() && userIs.isEmpty()) {
	        Friend_Request friend = Friend_Request.builder()
	                .userId(user)
	                .sendUserId(sendUser)
	                .friendState(0)
	                .build();
	        friendRequestRepository.save(friend);
	        return friend;
	    } else if (!userIs.isEmpty()) {
	        throw new RuntimeException("이미 친구요청을 보냈습니다");
	    } else {
	        Friend addFriend = Friend.builder()
	                .userId(user)
	                .sendUserId(sendUser)
	                .friendState(1)
	                .build();
	        friendRepository.save(addFriend);
	        for (Friend_Request request : me) {
	            friendRequestRepository.deleteById(request.getFriendRequestId());
	        }
	        return null;
	    }
	}
	
	/**
	 * 친구 요청 목록 보기 
	 * @param userId
	 * @return
	 */
	public List<Friend_Request> getFriendRequests(User userId){
		return friendRequestRepository.findByUserId(userId);
	}
	
	/**
	 * 친구 요청 수락  
	 * @param friend
	 * @return
	 */
	public Friend insert(AddFriend friend) {
		
		Friend addFriend = Friend.builder()
				.userId(friend.getUserId())
				.sendUserId(friend.getSendUserId())
				.friendState(1)
				.build();
		return friendRepository.save(addFriend);
	}
	
	/**
	 * 친구 요청 테이블에서 삭제 
	 * @param friendRequestId
	 */
	public void remove(Long friendRequestId) {
		friendRequestRepository.deleteById(friendRequestId);
	}
	
	/**
	 * 받은 친구 요청 카운트 
	 * @param userId
	 * @return
	 */
	public long getCountByUserId(User userId) {
		return friendRequestRepository.countByUserId(userId);
	}
	
	/**
	 * 친구 삭제 
	 * @param friendId
	 */
	public void removeFriend(Long friendId) {
		friendRepository.deleteById(friendId);
	}
	
	/**
	 * 친구 목록 중 받은 친구 카운트 
	 * @param userId
	 */
	public long countByUserId(User userId) {
		return friendRepository.countByUserId(userId);
	}
	
	/**
	 * 친구 목록 중 친구 카운트 
	 * @param sendUserId
	 */
	public long countBySendUserId(User sendUserId) {
		return friendRepository.countBySendUserId(sendUserId);
	}

}
