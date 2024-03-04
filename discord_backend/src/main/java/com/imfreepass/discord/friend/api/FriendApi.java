package com.imfreepass.discord.friend.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.imfreepass.discord.friend.api.response.ViewDistinctFriend;
import com.imfreepass.discord.friend.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.imfreepass.discord.friend.api.request.AddFriend;
import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.friend.api.response.ViewFriend;
import com.imfreepass.discord.friend.api.response.ViewFriendResponse;
import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.friend.entity.FriendRequest;
import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.swing.text.View;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/friend")
@Log4j2
public class FriendApi {

	private final FriendService friendService;
	private final UserService userService;


	/**
	 * 친구신청
	 *
	 * @param send
	 * @return
	 */
	@PostMapping("/request")
	public ResponseEntity<String> sendFriendRequest(@RequestBody SendFriendRequest send) {
		friendService.sendFriendRequest(send);
		return ResponseEntity.ok("친구신청이 완료되었습니다");
	}

	/**
	 * 받은 친구 요청 목록 보기
	 *
	 * @param fromUserId
	 * @return
	 */
	@GetMapping("/response/{fromUserId}")
	public ResponseEntity<List<ViewFriendResponse>> getFriendRequests(@PathVariable(name = "fromUserId") Long fromUserId) {
		List<FriendRequest> friendRequests = friendService.getFriendRequests(fromUserId);
		List<ViewFriendResponse> friendResponse = friendRequests.stream()
				.map(friendRequest -> {
					ViewUser viewUser = userService.converterViewUser(friendRequest.getSendUserId());
					return new ViewFriendResponse(friendRequest.getFromUserId(), friendRequest.getSendUserId(), viewUser, friendRequest.getFriendState());
				})
				.collect(Collectors.toList());
		return ResponseEntity.ok(friendResponse);
	}

	/**
	 * 받은 친구 목록 갯수
	 *
	 * @param fromUserId
	 * @return
	 */
	@GetMapping("/count/{fromUserId}")
	public long countFriendRequests(@PathVariable(name = "fromUserId") Long fromUserId) {
		return friendService.getCountByFromUserId(fromUserId);
	}

	/**
	 * 친구 요청 삭제
	 *
	 * @param friendRequestId
	 * @return
	 */
	@DeleteMapping("/request/{friendRequestId}")
	public ResponseEntity<String> removeFriendRequest(@PathVariable(name = "friendRequestId") Long friendRequestId) {
		friendService.remove(friendRequestId);
		return ResponseEntity.ok("친구 요청 삭제가 완료되었습니다");
	}

	/**
	 * 친구 목록
	 *
	 * @param fromUserId
	 * @return
	 */
	@GetMapping("/list/{fromUserId}")
	public List<ViewDistinctFriend> viewFriend(@PathVariable(name = "fromUserId") Long fromUserId) {
		Long sendUserId = fromUserId;
		return friendService.getViewFriendsList(sendUserId, fromUserId);
	}

	/**
	 * 온라인 친구 목록
	 *
	 * @param fromUserId
	 * @return
	 */
	@GetMapping("/online/{fromUserId}")
	public List<ViewDistinctFriend> getOnlineFriendsList(@PathVariable(name = "fromUserId") Long fromUserId) {
		Long sendUserId = fromUserId;
		return friendService.getViewOnlineFriend(sendUserId, fromUserId);
	}


	/**
	 * 온라인 친구 목록 카운트
	 *
	 * @param fromUserId
	 * @return
	 */
	@GetMapping("/online/count/{fromUserId}")
	public long getOnlineFriends(@PathVariable(name = "fromUserId") Long fromUserId) {
		Long sendUserId = fromUserId;
		List<ViewDistinctFriend> getCountFriends = friendService.getViewOnlineFriend(fromUserId, sendUserId);
		return getCountFriends.size();
	}

	/**
	 * 내 친구 목록 카운트
	 *
	 * @param fromUserId
	 * @return
	 */
	@GetMapping("/list/count/{fromUserId}")
	public long getFriends(@PathVariable(name = "fromUserId") Long fromUserId) {
		long user = friendService.countByUserId(fromUserId);
		long sendUserId = friendService.countBySendUserId(fromUserId);
		return user + sendUserId;
	}

	/**
	 * 친구 삭제
	 *
	 * @param friendId
	 * @return
	 */
	@DeleteMapping("/{friendId}")
	public ResponseEntity<String> removeFriend(@PathVariable(name = "friendId") Long friendId) {
		try {
			friendService.removeFriend(friendId);
			return ResponseEntity.ok("친구삭제가 완료되었습니다");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 삭제에 오류가 발생했습니다");
		}
	}
}
