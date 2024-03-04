
package com.imfreepass.discord.user.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import com.imfreepass.discord.user.api.request.*;
import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.exception.NoSuchException;
import com.imfreepass.discord.user.service.UserImgService;
import com.imfreepass.discord.user.service.UserService;
import jakarta.persistence.Id;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.imfreepass.discord.friend.api.request.SeachUser;
import com.imfreepass.discord.user.api.response.LoginResponse;

import com.imfreepass.discord.user.config.jwt.TokenProvider;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.entity.UserImg;
import com.imfreepass.discord.user.service.MailService;


import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApi {

    private static final int DEFAULT_ACCESS_EXPIRATION_MINUTES = 30;
    private static final int DEFAULT_REFRESH_EXPIRATION_DAYS = 10;

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final MailService mailService;
    private final UserImgService imgService;

    /**
     * 모든 user 보기
     *
     * @return
     */
    @GetMapping("/register")
    @CrossOrigin
    public List<User> viewAllUser() {
        return userService.allUser();
    }

    /**
     * 회원가입 후 기본 프로필 이미지 등록
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUser user) {
        userService.insert(user);
        return ResponseEntity.ok(user.getEmail());
    }

    /**
     * 로그인
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUser user) {
        User loginUser = userService.findByEmail(user.getEmail()).orElseThrow();
        boolean isPasswordMatched = encoder.matches(user.getPassword(), loginUser.getPassword());
        if (isPasswordMatched) {
            // accessToken
            Duration accessTime = Duration.ofMinutes(DEFAULT_ACCESS_EXPIRATION_MINUTES);
            String accessToken = tokenProvider.makeToken(user, accessTime);
            // refreshToken
            Duration refreshTokenTime = Duration.ofDays(DEFAULT_REFRESH_EXPIRATION_DAYS);
            String refreshToken = tokenProvider.makeToken(user, refreshTokenTime);
            userService.saveRefreshToken(user.getEmail(), refreshToken);
            userService.modifyState(loginUser.getUserId(), loginUser.getPreState());
            LoginResponse response = new LoginResponse(user.getEmail(), accessToken, refreshToken, "로그인 성공입니다.");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    /**
     * accessToken 재발급
     *
     * @param user
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody LoginUser user) {
        Optional<User> viewUser = userService.findByEmail(user.getEmail());
        String refreshToken = viewUser.get().getRefreshToken();
        boolean isRefreshTokenEquals = refreshToken.equals(user.getRefreshToken());
        if (isRefreshTokenEquals) {
            Duration accessTime = Duration.ofMinutes(DEFAULT_ACCESS_EXPIRATION_MINUTES);
            String accessToken = tokenProvider.makeToken(user, accessTime);
            LoginResponse response = new LoginResponse(user.getEmail(), accessToken, "accessToken 재발급되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 로그아웃
     *
     * @param user
     * @return
     */
    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<String> logout(@RequestBody Logout user) {
        Optional<User> view = userService.findByEmail(user.getEmail());
        Long userId = view.get().getUserId();
        userService.logout(view);
        return ResponseEntity.ok("로그아웃 완료입니다.");
    }

    /**
     * 비밀번호 변경 이메일 보내기
     *
     * @param emailDto
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
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

    /**
     * 비밀번호 변경 이메일 보기
     *
     * @param tokenLink
     * @return
     */
    @GetMapping("/email/{tokenLink}")
    public String chagePw(@PathVariable(name = "tokenLink") String tokenLink) {

        return "테스트";
    }

    /**
     * 이메일로 비밀번호 변경
     *
     * @param pwDto
     * @return
     */
    @PutMapping("/email")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordChage pwDto) {
        User user = userService.findByEmail(pwDto.getEmail()).orElseThrow(() -> new NoSuchException("존재하지 않는 이메일입니다."));
        pwDto.setPassword(BCrypt.hashpw(pwDto.getPassword(), BCrypt.gensalt()));
        userService.modifyPw(user.getUserId(), pwDto.getPassword());
        return ResponseEntity.ok("변경이 완료되었습니다.");
    }

    /**
     * 마이페이지 비밀번호 변경
     *
     * @param pwDto
     * @return
     */
    @PutMapping("/user/changepw")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChage pwDto) {
        User user = userService.findByEmail(pwDto.getEmail()).orElseThrow(() -> new NoSuchException("존재하지 않는 이메일입니다."));
        boolean isPasswordMatches = encoder.matches(pwDto.getPassword(), user.getPassword());
        if (isPasswordMatches) {
            user.setPassword(BCrypt.hashpw(pwDto.getCurrentPassword(), BCrypt.gensalt()));
            userService.modifyPw(user.getUserId(), user.getPassword());
            return ResponseEntity.ok("변경이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("변경이 실패되었습니다");
        }
    }

    /**
     * 닉네임 변경
     *
     * @param nickDto
     * @return
     */
    @PutMapping("/user/changenickname")
    public ResponseEntity<String> changeNickname(@RequestBody NicknameChange nickDto) {
        User user = userService.findByEmail(nickDto.getEmail()).orElseThrow(() -> new NoSuchException("존재하지 않는 이메일입니다."));
        user.setNickname(nickDto.getNickname());
        userService.modifyNickname(user.getUserId(), user.getNickname());
        return ResponseEntity.ok("닉네임 수정 완료");
    }

    /**
     * 프로필 등록
     *
     * @param files
     * @param userId
     * @return
     */
    @PostMapping("/user/profile/img")
    public ResponseEntity<String> addProfileImg(
            @RequestPart(value = "imgs", required = false) List<MultipartFile> files,
            @RequestPart(value = "user", required = false) Long userId) {
        UserImg img = imgService.findUserImg(userId).orElseThrow(() -> new NoSuchException("존재하지 않는 이미지입니다."));
        imgService.registerImg(img, userId, files);
        return ResponseEntity.ok("이미지가 등록되었습니다.");
    }

    /**
     * 기본 프로필로 변경
     *
     * @param profile
     * @return
     */
    @PostMapping("/user/profile/img/reset")
    public ResponseEntity<String> resetProfile(@RequestBody AddAndRemoveProfile profile) {
        imgService.defalutProfile(profile);
        return ResponseEntity.ok("이미지 삭제 후 기본 프로필로 변경되었습니다.");
    }

    /**
     * 프로필 1명 조회
     *
     * @param userId
     * @return
     */
    @GetMapping("/user/profile/{user_id}")
    public Optional<UserImg> viewProfile(@PathVariable("userId") Long userId) {
        return imgService.findUserImg(userId);
    }

    /**
     * 프로필 상태 변경
     *
     * @param user
     * @return
     */
    @PutMapping("/user/state")
    public int changeStatus(@RequestBody StateChange user) {
        return userService.modifyState(user.getUserId(), user.getState());
    }

    /**
     * 유저 검색
     *
     * @param req
     * @return
     */
    @GetMapping("/user/serach")
    public ResponseEntity<ViewUser> serachUsers(@RequestBody SeachUser req) {
        Optional<User> getNickname = userService.findByNickname(req.getNickname());
        if (getNickname.isEmpty()) {
            Optional<User> getUserHash = userService.findByUserHash(req.getUserHash());
            return getUserHash.map(user -> ResponseEntity.ok(userService.convertToViewUser(user)))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return getNickname.map(user -> ResponseEntity.ok(userService.convertToViewUser(user)))
                    .orElse(ResponseEntity.notFound().build());
        }
    }


}
