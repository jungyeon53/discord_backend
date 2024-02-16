package com.imfreepass.discord.user.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.imfreepass.discord.user.api.request.CreateUser;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// 중복검사 후 회원가입
	public User insert(CreateUser req) {
		Optional<User> userOptional = userRepository.findByEmail(req.getEmail());
		if(!userOptional.isPresent()) {
			User user = User.builder()
					.email(req.getEmail())
					.password(bCryptPasswordEncoder.encode(req.getPassword()))
					.nickname(req.getNickname())
					.userHash(req.getUser_hash())
					.birth(req.getBirth())
					.joinDate(ZonedDateTime.now())
					.build();
			userRepository.save(user);
			return user;
		} else {
			throw new DataIntegrityViolationException("중복된 이메일 주소입니다.");
		}
	}
	
	// 유저 전체 조회 
	public List<User> allUser(){
		return userRepository.findAll();
	}
	
	// email 조회 
	public Optional<User> selectEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	// refeshToken 저장 
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
	
	 // 로그아웃
    public int removeRefreshToken(String email) {
        return userRepository.clearTokenByEmail(email);
    }
    
    // 비밀번호 변경 
	public int modifyPw(Long user_id, String password) {
		return userRepository.updatePassword(user_id, password);
		
	}
	
	// email로 유저 조회 
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	// 닉네임 변경 
	public int modifyNickname(Long user_id, String nickname) {
		return userRepository.updateNickname(user_id, nickname);
	}
}
