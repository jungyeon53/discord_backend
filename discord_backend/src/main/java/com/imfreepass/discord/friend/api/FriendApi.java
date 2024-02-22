package com.imfreepass.discord.friend.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imfreepass.discord.friend.api.request.AddFriend;
import com.imfreepass.discord.friend.api.request.SendFriendRequest;
import com.imfreepass.discord.friend.api.response.ViewFriend;
import com.imfreepass.discord.friend.api.response.ViewFriendResponse;
import com.imfreepass.discord.friend.entity.Friend;
import com.imfreepass.discord.friend.entity.Friend_Request;
import com.imfreepass.discord.friend.service.FriendService;
import com.imfreepass.discord.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/friend")
@Log4j2
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
	 * 친구 요청 목록 보기 
	 * @param userId
	 * @return
	 */
	@GetMapping("/response/{userId}")
	public List<Friend_Request> getFriendRequests(@PathVariable(name = "userId") User userId){
		return friendService.getFriendRequests(userId);
	}
	
	/**
	 * 받은 친구 목록 갯수 
	 * @param userId
	 * @return
	 */
	@GetMapping("/count/{userId}")
	public long countFriendRequests(@PathVariable(name = "userId") User userId) {
		return friendService.getCountByUserId(userId);
	}
	
	/**
	 * 친구 요청 삭제 
	 * @param friendRequestId
	 * @return
	 */
	@DeleteMapping("/request/{friendRequestId}")
	public ResponseEntity<String> removeFriendRequest(@PathVariable(name = "friendRequestId") Long friendRequestId){
		try {
			friendService.remove(friendRequestId);
			return ResponseEntity.ok("친구 요청 삭제가 완료되었습니다");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 요청 삭제에 오류가 발생했습니다");
		}
	}
	
	// 친구 목록 
	
	
	/**
	 * 내 친구 목록 카운트 
	 * @param view
	 * @return
	 */
	@GetMapping("/list/{userId}")
	public long getFriends(@PathVariable(name = "userId") User userId) {
		long user = friendService.countByUserId(userId);
		long sendUserId = friendService.countBySendUserId(userId);
		return user + sendUserId;
	}
	
	/**
	 * 친구 삭제
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
	
//	/**
//	 * 친구 요청 수락 
//	 * 테스트 안했음 / 사용 안할 예정 
//	 * @param friend
//	 * @return
//	 */
//	@PostMapping("/friend")
//	@Transactional
//	public ResponseEntity<String> acceptFriendRequest(@RequestBody AddFriend friend){
//		try {
//			friendService.remove(friend.getFriendRequestId());
//			friendService.insert(friend);
//			return ResponseEntity.ok("친구추가가 완료되었습니다");
//		}catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 추가에 오류가 발생했습니다");
//		}
//	}
}
