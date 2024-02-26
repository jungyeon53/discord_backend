package com.imfreepass.discord.user.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.imfreepass.discord.user.api.request.CreateUser;
import com.imfreepass.discord.user.entity.State;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.entity.UserImg;
import com.imfreepass.discord.user.repository.UserRepository;
import com.imfreepass.discord.user.repository.StateRepository;
import com.imfreepass.discord.user.repository.UserImgRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
	
	private final UserRepository userRepository;
	private final UserImgRepository imgRepository;
	private final StateRepository stateRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/**
	 * 중복검사 후 회원가입
	 * @param req
	 * @return
	 */
	public User insert(CreateUser req) {
	    // 중복된 이메일 주소 검사
	    userRepository.findByEmail(req.getEmail())
	            .ifPresent(email -> {
	                throw new DataIntegrityViolationException("중복된 이메일 주소입니다.");
	            });
	    // 유저 해시 중복 검사
	    userRepository.findByUserHash(req.getUserHash())
	            .ifPresent(user_hash -> {
	                throw new RuntimeException("중복된 유저해시입니다.");
	            });
	    // 상태 정보 가져오기
	    State userState = stateRepository.findById(4L)
	            .orElseThrow(() -> new IllegalStateException("상태를 찾을 수없습니다."));
	    // 회원가입
	    User user = User.builder()
	            .email(req.getEmail())
	            .password(bCryptPasswordEncoder.encode(req.getPassword()))
	            .nickname(req.getNickname())
	            .userHash(req.getUserHash())
	            .birth(req.getBirth())
	            .joinDate(ZonedDateTime.now())
	            .stateId(userState)
	            .preState(1)
	            .build();
	    User savedUser = userRepository.save(user);
	    insertRandom(savedUser);

	    return savedUser;
	}

	
	/**
	 * 랜덤 기본 이미지
	 * @param save
	 * @return
	 */
	public UserImg insertRandom(User save) {
		Random random = new Random();
		int randomNum = random.nextInt(5)+1;
		String original = randomNum + ".jpg";
		String path = "src/main/resources/static/img/default_profile/" + original;
		UserImg img = UserImg.builder()
				.userId(save)
				.original(original)
				.path(path)
				.build();
		return imgRepository.save(img);
	}
	
	/**
	 * 유저 전체 조회 
	 * @return
	 */
	public List<User> allUser(){
		return userRepository.findAll();
	}
	
	/**
	 * acessToken 재발급 
	 * @param email
	 * @param refreshToken
	 * @return
	 */
	public User saveRefreshToken(String email, String refreshToken) {
		Optional<User> user = userRepository.findByEmail(email);
		if(user.isPresent()) {
			User member = user.get();
			member.setRefreshToken(refreshToken);
			return userRepository.save(member);
		} else {
			throw new NoSuchElementException("유저가 없습니다");
		}
	}
	
	 /**
	  * 로그아웃
	  * @param email
	  * @return
	  */
    public int removeRefreshToken(String email) {
        return userRepository.clearTokenByEmail(email);
    }
    
    /**
     * 비밀번호 변경 
     * @param user_id
     * @param password
     * @return
     */
	public int modifyPw(Long user_id, String password) {
		return userRepository.updatePassword(user_id, password);
		
	}
	
	/**
	 * email로 유저 조회 
	 * @param email
	 * @return
	 */
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	/**
	 * 닉네임 변경 
	 * @param user_id
	 * @param nickname
	 * @return
	 */
	public int modifyNickname(Long user_id, String nickname) {
		return userRepository.updateNickname(user_id, nickname);
	}
	
	/**
	 * 상태 변경 
	 * @param stateId
	 * @param userId
	 * @return
	 */
	public int modifyState(State stateId, Long userId) {
		return userRepository.updateState(userId, stateId);
	}
	
	/**
	 * 이전 상태 변경 
	 * @param userId
	 * @param preState
	 */
	public void modifyPreState(Long userId, int preState) {
		userRepository.updatePreState(userId, preState);
	}
	
	/** userId로 조회 
	 * @param userId
	 * @return
	 */
	public Optional<User> findByUserId(Long userId) {
		return userRepository.findById(userId);
	}
	
	/**
	 * userHash 검색 
	 * @param userHash
	 * @return
	 */
	public Optional<User> findByUserHash(String userHash){
		return userRepository.findByUserHash(userHash);
	}
	
	/**
	 * 닉네임 검색 
	 * @param nickname
	 * @return
	 */
	public Optional<User> findByNickname(String nickname){
		return userRepository.findByNickname(nickname);
	}
}
