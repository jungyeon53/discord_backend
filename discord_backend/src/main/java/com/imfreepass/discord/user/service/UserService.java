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
import com.imfreepass.discord.user.entity.User_Img;
import com.imfreepass.discord.user.repository.UserRepository;
import com.imfreepass.discord.user.repository.StateRepository;
import com.imfreepass.discord.user.repository.UserImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
		Optional<User> userOptional = userRepository.findByEmail(req.getEmail());
		Optional<State> userState = stateRepository.findById(1L);
		if(!userOptional.isPresent()) {
			User user = User.builder()
					.email(req.getEmail())
					.password(bCryptPasswordEncoder.encode(req.getPassword()))
					.nickname(req.getNickname())
					.userHash(req.getUser_hash())
					.birth(req.getBirth())
					.joinDate(ZonedDateTime.now())
					.stateId(userState.get())
					.preState(1)
					.build();
			User save = userRepository.save(user);
			return save;
		} else {
			throw new DataIntegrityViolationException("중복된 이메일 주소입니다.");
		}
	}
	
	/**
	 * 랜덤 기본 이미지
	 * @param save
	 * @return
	 */
	public User_Img insertRandom(User save) {
		Random random = new Random();
		int randomNum = random.nextInt(5)+1;
		String original = randomNum + ".jpg";
		String path = "src/main/resources/static/img/default_profile/" + original;
		User_Img img = User_Img.builder()
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
	 * refeshToken 저장 
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
}
