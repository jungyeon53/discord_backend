package com.imfreepass.discord.friend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.imfreepass.discord.friend.api.response.ViewDistinctFriend;
import com.imfreepass.discord.friend.api.response.ViewFriend;
import com.imfreepass.discord.friend.exception.DuplicateRequestException;
import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.imfreepass.discord.friend.api.request.AddFriend;
import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.friend.entity.FriendRequest;
import com.imfreepass.discord.friend.repository.FriendRepository;
import com.imfreepass.discord.friend.repository.FriendRequestRepository;
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
	private final UserService userService;

	/**
	 * 친구 요청
	 *
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
	 *
	 * @param fromUserId
	 * @param sendUserId
	 */
	private void friendRequestDuplicate(Long fromUserId, Long sendUserId) {
		boolean isValidateDuplicateRequest = friendRequestRepository.findByFromUserIdAndSendUserId(fromUserId, sendUserId).isPresent();
		if (isValidateDuplicateRequest) {
			throw new DuplicateRequestException("이미 친구신청이 완료된 내역입니다.");
		}
	}

	/**
	 * 요청여부 체크
	 *
	 * @param request
	 */
	@Transactional
	public void processFriendRequest(SendFriendRequest request) {
		boolean isFriendRequest = friendRequestRepository.existsByFromUserId(request.getSendUserId());
		if (isFriendRequest) {
			friendRepository.save(Friend.FriendInsert(request));
			FriendRequest friendRequest = friendRequestRepository.findByFromUserIdAndSendUserId(request.getSendUserId(), request.getFromUserId()).orElseThrow();
			friendRequestRepository.deleteById(friendRequest.getFriendRequestId());
		} else {
			friendRequestRepository.save(FriendRequest.FriendRequestInsert(request));
		}
	}


	/**
	 * 친구 요청 목록 보기
	 *
	 * @param fromUserId
	 * @return
	 */
	public List<FriendRequest> getFriendRequests(Long fromUserId) {
		return friendRequestRepository.findByFromUserId(fromUserId);
	}

	/**
	 * 친구 요청 수락
	 *
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
	 *
	 * @param friendRequestId
	 */
	public void remove(Long friendRequestId) {
		friendRequestRepository.deleteById(friendRequestId);
	}

	/**
	 * 받은 친구 요청 카운트
	 *
	 * @param fromUserId
	 * @return
	 */
	public long getCountByFromUserId(Long fromUserId) {
		return friendRequestRepository.countByFromUserId(fromUserId);
	}

	/**
	 * 친구 삭제
	 *
	 * @param friendId
	 */
	public void removeFriend(Long friendId) {
		friendRepository.deleteById(friendId);
	}

	/**
	 * 친구 목록 중 받은 친구 카운트
	 *
	 * @param fromUserId
	 */
	public long countByUserId(Long fromUserId) {
		return friendRepository.countByFromUserId(fromUserId);
	}

	/**
	 * 친구 목록 중 친구 카운트
	 *
	 * @param sendUserId
	 */
	public long countBySendUserId(Long sendUserId) {
		return friendRepository.countBySendUserId(sendUserId);
	}

	/**
	 * 친구 목록에서 찾기
	 *
	 * @param fromUserId
	 * @param sendUserId
	 * @return
	 */
	public List<Friend> viewUser(Long fromUserId, Long sendUserId) {
		return friendRepository.findByFromUserIdOrSendUserId(fromUserId, sendUserId);

	}

	/**
	 * 친구 목록 리스트 (나 포함)
	 *
	 * @param fromUserId
	 * @param sendUserId
	 * @return
	 */
	public List<ViewFriend> getViewFriends(Long fromUserId, Long sendUserId) {
		List<Friend> friends = viewUser(fromUserId, sendUserId);
		return friends.stream()
				.map(friend -> {
					ViewUser sendUser = userService.converterViewUser(friend.getSendUserId());
					ViewUser fromUser = userService.converterViewUser(friend.getFromUserId());
					return new ViewFriend(friend.getFriendId(), sendUser, fromUser, friend.getFriendState());
				})
				.collect(Collectors.toList());
	}

	/**
	 * 친구 목록
	 * @param fromUserId
	 * @param sendUserId
	 * @return
	 */
	public List<ViewDistinctFriend> getViewFriendsList(Long fromUserId, Long sendUserId) {
		List<ViewFriend> viewFriends = getViewFriends(fromUserId, sendUserId);
		return getViewFriends(fromUserId, sendUserId).stream()
						.flatMap(friend -> Stream.of(friend.getFromUserId(), friend.getSendUserId()))
						.filter(user -> !user.getUserId().equals(fromUserId) && !user.getUserId().equals(sendUserId))
						.distinct()
						.map(user -> new ViewDistinctFriend(user.getUserId(), user, user.getState().ordinal()))
						.collect(Collectors.toList());
	}

	/**
	 * 온라인 친구 목록
	 * @param fromUserId
	 * @param sendUserId
	 * @return
	 */
	public List<ViewDistinctFriend> getViewOnlineFriend(Long fromUserId, Long sendUserId) {
		List<ViewFriend> viewFriends = getViewFriends(fromUserId, sendUserId);
		return getViewFriends(fromUserId, sendUserId).stream()
						.flatMap(friend -> Stream.of(friend.getFromUserId(), friend.getSendUserId()))
						.filter(user -> !user.getUserId().equals(fromUserId) && !user.getUserId().equals(sendUserId))
						.distinct()
						.filter(user -> {
							User.State fromUserStateId = user.getState();
							return fromUserStateId != User.State.IDLE;
						})
						.map(user -> new ViewDistinctFriend(user.getUserId(), user, user.getState().ordinal()))
						.collect(Collectors.toList());


	}
}
