package com.imfreepass.discord.friend.service;

import java.util.List;
import java.util.Optional;

import com.imfreepass.discord.exception.DuplicateException;
import com.imfreepass.discord.user.exception.NoSuchException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.imfreepass.discord.friend.api.request.AddFriend;
import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.friend.entity.FriendRequest;
import com.imfreepass.discord.friend.repository.FriendRepository;
import com.imfreepass.discord.friend.repository.FriendRequestRepository;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.repository.UserRepository;

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
	 * 친구 요청
	 * @param request
	 * @return
	 */
	public void sendFriendRequest(SendFriendRequest request) {
//		User fromUserId =  userRepository.findById(request.getFromUserId()).orElseThrow(() -> new NoSuchException("받은 유저를 찾을 수 없습니다."));
//		User sendUserId = userRepository.findById(request.getSendUserId()).orElseThrow(() -> new NoSuchException("보낸 유저를 찾을 수 없습니다."));
		friendRequestDuplicate(request.getFromUserId(), request.getSendUserId());
		processFriendRequest(request);
	}

	/**
	 * 친구 요청 중복 체크
	 * @param fromUserId
	 * @param sendUserId
	 */
	private void friendRequestDuplicate(Long fromUserId, Long sendUserId){
		boolean isValidateDuplicateRequest = friendRequestRepository.findByFromUserIdAndSendUserId(fromUserId, sendUserId).isPresent();
		if(isValidateDuplicateRequest) {
			throw new DuplicateException("이미 친구신청이 완료된 내역입니다.");
		}
	}

	/**
	 *	요청여부 체크
	 * @param request
	 */
	@Transactional
	public void processFriendRequest(SendFriendRequest request){
		boolean isFriendRequest = friendRequestRepository.existsByFromUserId(request.getSendUserId());
		if(isFriendRequest){
			friendRepository.save(Friend.FriendInsert(request));
			FriendRequest friendRequest = friendRequestRepository.findByFromUserIdAndSendUserId(request.getSendUserId(),request.getFromUserId()).orElseThrow();
			friendRequestRepository.deleteById(friendRequest.getFriendRequestId());
		} else {
			friendRequestRepository.save(FriendRequest.FriendRequestInsert(request));
		}
	}


	/**
	 * 친구 요청 목록 보기
	 * @param userId
	 * @return
	 */
//	public List<FriendRequest> getFriendRequests(Long userId){
//		return friendRequestRepository.findByUserId(userId);
//	}

	/**
	 * 친구 요청 수락
	 * @param friend
	 * @return
	 */
	public Friend insert(AddFriend friend) {

		Friend addFriend = Friend.builder()
				.fromUserId(friend.getUserId())
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
//	public long getCountByUserId(User userId) {
//		return friendRequestRepository.countByUserId(userId);
//	}

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
//	public long countByUserId(User userId) {
//		return friendRepository.countByUserId(userId);
//	}

	/**
	 * 친구 목록 중 친구 카운트
	 * @param sendUserId
	 */
	public long countBySendUserId(User sendUserId) {
		return friendRepository.countBySendUserId(sendUserId);
	}

	/**
	 * 친구 목록
	 * @param userId
	 * @param sendUserId
	 * @return
	 */
//	public List<User> viewUser(Long userId, Long sendUserId){
//		 List<Friend> friends = friendRepository.findByUserIdOrSendUserId(userId,sendUserId);
//		    return friends.stream()
//		            .flatMap(friend -> Stream.of(friend.getUserId(), friend.getSendUserId()))
//		            .filter(user -> !user.getUserId().equals(userId.getUserId()))
//		            .distinct()
//		            .collect(Collectors.toList());
//	}
}
