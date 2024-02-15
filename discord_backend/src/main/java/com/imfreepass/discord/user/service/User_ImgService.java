package com.imfreepass.discord.user.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imfreepass.discord.user.api.request.AddProfile;
import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.entity.User_Img;
import com.imfreepass.discord.user.repository.UserRepository;
import com.imfreepass.discord.user.repository.User_ImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class User_ImgService {
	
	private final UserRepository userRepository;
	private final User_ImgRepository imgRepository;
	
	// 이미지 추가 static/img/user/{user_id}/profile.jpg
	 public void addProfile(List<MultipartFile> files, Long user_id) {
	        List<String> profile = files.stream().map(file -> {
	            String original = file.getOriginalFilename();
	            // 상대경로
	            String relativePath = "img/user/" + user_id + "/" + original;
	            // 절대경로
	            String absolutePath = "static/" + relativePath;

	            try {
	                Path directoryPath = Paths.get("static/img/user/" + user_id);
	                Files.createDirectories(directoryPath);
	                Path filePath = Paths.get(absolutePath);
	                Files.write(filePath, file.getBytes());
	                User user = userRepository.findById(user_id)
	                        .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 사용자를 찾을 수 없습니다: " + user_id));
	                User_Img addProfile = User_Img.builder()
	                        .user_id(user)
	                        .original(original)
	                        .path(relativePath)
	                        .build();

	                imgRepository.save(addProfile);
	                return relativePath; 
	            } catch (IOException e) {
	                e.printStackTrace();
	                return null;
	            }
	        }).collect(Collectors.toList());
	    }
	}
