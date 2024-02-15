
package com.imfreepass.discord.api;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.imfreepass.discord.api.request.CreateUser;
import com.imfreepass.discord.api.request.PasswordChage;
import com.imfreepass.discord.api.request.SendEmail;
import com.imfreepass.discord.api.response.LoginResponse;
import com.imfreepass.discord.api.response.LoginUser;
import com.imfreepass.discord.config.jwt.TokenProvider;
import com.imfreepass.discord.entity.User;
import com.imfreepass.discord.service.MailService;
import com.imfreepass.discord.service.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApi {

	private final UserService userService;
	private final PasswordEncoder encoder;
	private final TokenProvider tokenProvider;
	private final MailService mailService;

	@GetMapping("/register")
	@CrossOrigin
	public List<User> viewAllUser() {
		return userService.allUser();
	}

	// 회원가입
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody CreateUser user) {
		try {
			userService.insert(user);
			return ResponseEntity.ok("회원가입 완료" + user.getEmail());
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
		if (view.isPresent() && encoder.matches(user.getPassword(), view.get().getPassword())) {
			// acessToken 발급
			Duration accessTime = Duration.ofMinutes(30);
			String accessToken = tokenProvider.makeToken(user, accessTime);
			// refreshToken 발급
			Duration refeshTime = Duration.ofDays(10);
			String refeshToken = tokenProvider.makeToken(user, refeshTime);
			// refreshToken 저장
			userService.saveRefreshToken(user.getEmail(), refeshToken);
			LoginResponse response = new LoginResponse(user.getEmail(), accessToken, refeshToken, "로그인성공");
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// refreshToken 재발급
	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken(@RequestBody LoginUser user) {

		Optional<User> view = userService.selectEmail(user.getEmail());
		String refreshToken = view.get().getRefreshToken();
		if (refreshToken.equals(user.getRefreshToken())) {
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
	public ResponseEntity<String> logout(@RequestBody LoginUser user) {
		try {
			userService.removeRefreshToken(user.getEmail());
			return ResponseEntity.ok("로그아웃 완료");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 실패");
		}
	}

	// 비밀번호 변경 이메일 보내기
	@PostMapping("/email")
	public String mailConfirm(@RequestBody SendEmail emailDto) throws MessagingException, UnsupportedEncodingException {
		Optional<User> userOptional = userService.findByEmail(emailDto.getEmail());
		if (userOptional.isPresent()) {
			Long user_id = userOptional.get().getUser_id();
			String tokenLink = mailService.sendEmail(emailDto.getEmail(), user_id);
			return tokenLink;
		} else {
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "가입되지 않은 사용자입니다");
	    }
	}

	// 비밀번호 변경 이메일 보기
	@GetMapping("/email/{tokenLink}")
	public String chagePw(@PathVariable(name = "tokenLink") String tokenLink) {
		return "테스트";
	}

	// 비밀번호 변경 버튼 클릭
	@PutMapping("/email")
	public ResponseEntity<String> resetPassword(@RequestBody PasswordChage pwDto) {
		Optional<User> user = userService.selectEmail(pwDto.getEmail());
		if (user.isPresent()) {
			user.get().setPassword(BCrypt.hashpw(pwDto.getPassword(), BCrypt.gensalt()));

			userService.modifyPw(user.get().getUser_id(), user.get().getPassword());
			return ResponseEntity.ok("변경이 완료되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("변경이 실패되었습니다");
		}
	}

}
