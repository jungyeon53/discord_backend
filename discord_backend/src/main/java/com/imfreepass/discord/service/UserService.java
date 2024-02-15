package com.imfreepass.discord.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.imfreepass.discord.api.request.CreateUser;
import com.imfreepass.discord.entity.User;
import com.imfreepass.discord.repository.UserRepository;

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
					.user_hash(req.getUser_hash())
					.birth(req.getBirth())
					.join_date(ZonedDateTime.now())
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
	public void saveRefreshToken(String email, String refreshToken) {
		Optional<User> user = userRepository.findByEmail(email);
		if(user.isPresent()) {
			User member = user.get();
			member.setRefreshToken(refreshToken);
			userRepository.save(member);
		}
	}
	
	 // 로그아웃
    public void removeRefreshToken(String email) {
        userRepository.clearTokenByEmail(email);
    }
    
    // 비밀번호 변경 
	public void modifyPw(Long user_id, String password) {
		userRepository.updatePassword(user_id, password);
		
	}
	
	// email로 유저 조회 
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
