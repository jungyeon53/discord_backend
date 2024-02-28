package com.imfreepass.discord.user.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.exception.DuplicateEmailException;
import com.imfreepass.discord.user.exception.DuplicateNicknameException;
import jakarta.transaction.Transactional;
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
	private final UserImgService imgService;

	/**
	 * 회원가입
	 * @param request
	 * @return
	 */
	public void insert(CreateUser request) {
		validateEmail(request);
		validateUserHash(request);
		User user = User.registerUser(request, bCryptPasswordEncoder);
		User savedUser = userRepository.save(user);
		imgService.insertRandom(savedUser.getUserId());
	}


	/**
	 * 이메일 중복검사
	 * @param request
	 */
	public void validateEmail(CreateUser request) {
		boolean isValidateEmail = userRepository.findByEmail(request.getEmail()).isPresent();
		if(isValidateEmail) {
			throw new DuplicateEmailException("이미 가입된 이메일입니다.");
		}
	}

	/**
	 * 닉네임 중복검사
	 * @param request
	 */
	public void validateUserHash(CreateUser request) {
		boolean isValidateNickname = userRepository.findByUserHash(request.getNickname()).isPresent();
		if(isValidateNickname) {
			throw  new DuplicateNicknameException("이미 존재하는 닉네임입니다.");
		}
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
	public void saveRefreshToken(String email, String refreshToken) {
		Optional<User> user = userRepository.findByEmail(email);
		User member = user.get();
		member.setRefreshToken(refreshToken);
		userRepository.save(member);
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
	 * 상태변경
	 * @param userId
	 * @param preState
	 * @return
	 */
	public int  modifyState(Long userId, int preState) {
		int stateId = validateState(preState);
		return userRepository.updateState(userId, stateId);
	}

	/**
	 * preState 체크 후 state 상태 변경
	 * @param preState
	 * @return
	 */
	public int validateState(int preState){
		State stateId = new State();
		switch (preState) {
			case 1:
				stateId.setStateId(1);
				break;
			case 2:
				stateId.setStateId(2);
				break;
			case 3:
				stateId.setStateId(3);
				break;
			default:
				stateId.setStateId(1);
				break;
		}
		return preState;
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

	/**
	 * 로그아웃
	 * @param userId
	 * @param preState
	 * @param email
	 */
	@Transactional
	public void logout(Long userId, int preState, String email) {
		userRepository.updatePreState(userId, preState);
		modifyState(userId, 4);
		userRepository.clearTokenByEmail(email);
	}

	/**
	 * 유저 검색 반환 값
	 * @param user
	 * @return
	 */
	public ViewUser convertToViewUser(User user) {
		return new ViewUser(user.getUserId(), user.getStateId(), user.getEmail(), user.getNickname(), user.getUserHash());
	}

}
