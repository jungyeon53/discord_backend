package com.imfreepass.discord.friend.api;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.friend.api.response.ViewFriendResponse;
import com.imfreepass.discord.friend.entity.Friend_Request;
import com.imfreepass.discord.friend.service.FriendService;
import com.imfreepass.discord.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/friend")
public class FriendApi {
	
	private final FriendService friendService;
	
	/**
	 * 친구신청
	 * @param send
	 * @return
	 */
	@PostMapping("/request")
	public ResponseEntity<String> sendFriendRequest(@RequestBody SendFriendRequest send) {
		try {
			friendService.sendFriendRequest(send);
			return ResponseEntity.ok("친구신청이 완료되었습니다");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 신청에 오류가 발생했습니다");
		}
	}
	
	/**
	 * 
	 * @param res
	 * @return
	 */
	@GetMapping("/response/{userId}")
	public Optional<Friend_Request> getFriendRequests(@PathVariable(name = "userId") User userId){
		return friendService.getFriendRequests(userId);
	}
}
