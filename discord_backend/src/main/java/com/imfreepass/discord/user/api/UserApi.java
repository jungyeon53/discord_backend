
package com.imfreepass.discord.user.api;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.imfreepass.discord.user.api.request.AddAndRemoveProfile;
import com.imfreepass.discord.user.api.request.CreateUser;
import com.imfreepass.discord.user.api.request.NicknameChange;
import com.imfreepass.discord.user.api.request.PasswordChage;
import com.imfreepass.discord.user.api.request.SendEmail;
import com.imfreepass.discord.user.api.response.LoginResponse;
import com.imfreepass.discord.user.api.response.LoginUser;
import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.config.jwt.TokenProvider;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.service.MailService;
import com.imfreepass.discord.user.service.UserService;
import com.imfreepass.discord.user.service.User_ImgService;

import jakarta.mail.MessagingException;
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
	private final MailService mailService;
	private final User_ImgService imgService;

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입이 실패했습니다.");
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
			LoginResponse response = new LoginResponse(user.getEmail(), accessToken, refeshToken, "로그인 성공입니다.");
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
			LoginResponse response = new LoginResponse(user.getEmail(), accessToken, "accessToken 재발급되었습니다.");
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
			return ResponseEntity.ok("로그아웃 완료입니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 실패입니다.");
		}
	}

	// 비밀번호 변경 이메일 보내기
	@PostMapping("/email")
	public String mailConfirm(@RequestBody SendEmail emailDto) throws MessagingException, UnsupportedEncodingException {
		Optional<User> userOptional = userService.findByEmail(emailDto.getEmail());
		if (userOptional.isPresent()) {
			Long user_id = userOptional.get().getUserId();
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

			userService.modifyPw(user.get().getUserId(), user.get().getPassword());
			return ResponseEntity.ok("변경이 완료되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("변경이 실패되었습니다");
		}
	}

	// 비밀번호 변경 
	@PutMapping("/user/changepw")
	public ResponseEntity<String> changePassword(@RequestBody PasswordChage pwDto){
		Optional<User> optionUser = userService.findByEmail(pwDto.getEmail());
		if(optionUser.isPresent() && encoder.matches(pwDto.getCurrentPassword(), optionUser.get().getPassword())) {
			User user = optionUser.get();
			user.setPassword(BCrypt.hashpw(pwDto.getPassword(), BCrypt.gensalt()));
			userService.modifyPw(user.getUserId(), user.getPassword());
			return ResponseEntity.ok("변경이 완료되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("변경이 실패되었습니다");
		}
	}
	
	// 닉네임 변경 
	@PutMapping("/user/changenickname")
	public ResponseEntity<String> changeNickname(@RequestBody NicknameChange nickDto){
		Optional<User> optionUser = userService.findByEmail(nickDto.getEmail());
		if(optionUser.isPresent()) {
			User user = optionUser.get();
			user.setNickname(nickDto.getNickname());
			userService.modifyNickname(user.getUserId(), user.getNickname());
			return ResponseEntity.ok("닉네임 수정 완료");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("닉네임 수정 실패했습니다.");
		}
	}
	
	// 프로필 등록 
	@PostMapping("/user/profile/img")
	public ResponseEntity<String> addProfileImg(
			@RequestPart(value = "imgs", required = false) List<MultipartFile> files, 
			@RequestPart(value = "user", required = false) Long user_id
			){
		try {
			imgService.addProfile(files, user_id);
			return ResponseEntity.ok("이미지가 등록되었습니다.");
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 등록에 실패했습니다.");
		}
	}
	
	// 프로필 삭제 
	@PostMapping("/user/profile/img/remove")
	public ResponseEntity<String> removeProfile(@RequestBody AddAndRemoveProfile profile){
		try {
			imgService.imgRemove(profile.getUserImgId(), profile.getUserId());
			return ResponseEntity.ok("이미지가 삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제에 실패했습니다.");
		}
	}
	
}
