package com.imfreepass.discord.user.api;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imfreepass.discord.user.api.request.AddState;
import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.entity.State;
import com.imfreepass.discord.user.service.StateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StateApi {

	private final StateService stateService;
	
	/**
	 * 상태 추가 
	 * @param state
	 * @return
	 */
	@PostMapping("/state")
	public ResponseEntity<String> register(@RequestBody AddState state) {
		stateService.insert(state);
		return ResponseEntity.ok("상태 등록이 완료되었습니다.");
	}
}
