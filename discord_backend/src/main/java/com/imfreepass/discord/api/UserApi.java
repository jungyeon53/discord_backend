
package com.imfreepass.discord.api;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imfreepass.discord.api.request.CreateUser;
import com.imfreepass.discord.api.response.LoginResponse;
import com.imfreepass.discord.api.response.LoginUser;
import com.imfreepass.discord.api.response.ViewUser;
import com.imfreepass.discord.config.jwt.TokenProvider;
import com.imfreepass.discord.entity.User;
import com.imfreepass.discord.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class UserApi {
	
	private final UserService userService;
	private final PasswordEncoder encoder;
	private final TokenProvider tokenProvider;
	
	@GetMapping("/register")
	public List<User> viewAllUser() {
		return userService.allUser();
	}
	
	// 회원가입 
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody CreateUser user) {
		try {
			userService.insert(user);
			return ResponseEntity.ok("회원가입 완료"+user.getEmail());
		} catch (DataIntegrityViolationException e) {
	        // 중복된 이메일 예외 처리
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 이메일 주소입니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
		}
	}
	
	// 로그인 
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginUser user) {

		Optional<User> view = userService.selectEmail(user.getEmail());
		if(view.isPresent() && encoder.matches(user.getPassword(), view.get().getPassword())) {
			// acessToken 발급 
			Duration accessTime = Duration.ofMinutes(30);
			String accessToken = tokenProvider.makeToken(user, accessTime);
			// refreshToken 발급 
			Duration refeshTime  = Duration.ofDays(10);
			String refeshToken = tokenProvider.makeToken(user, refeshTime);
			// refreshToken 저장 
			userService.saveRefreshToken(user.getEmail(), refeshToken);
			LoginResponse response = new LoginResponse(user.getEmail(), accessToken, refeshToken, "로그인성공");
			return ResponseEntity.ok(response);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	// refreshToken 재발급 
	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken(@RequestBody LoginUser user){
	
		Optional<User> view = userService.selectEmail(user.getEmail());
		String refreshToken = view.get().getRefreshToken();
		if(refreshToken.equals(user.getRefeshToken())) {
			Duration accessTime = Duration.ofMinutes(30);
			String accessToken = tokenProvider.makeToken(user, accessTime);
			LoginResponse response = new LoginResponse(user.getEmail(), accessToken, "재발급성공");
			return ResponseEntity.ok(response);
		} else {
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestBody LoginUser user){
		try {
	        userService.removeRefreshToken(user.getEmail());
	        return ResponseEntity.ok("로그아웃 완료");
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 실패");
	    }
	}
	
}
