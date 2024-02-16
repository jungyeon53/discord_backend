package com.imfreepass.discord.user.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imfreepass.discord.user.api.request.AddAndRemoveProfile;
import com.imfreepass.discord.user.api.response.ViewUser;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.entity.User_Img;
import com.imfreepass.discord.user.repository.UserRepository;
import com.imfreepass.discord.user.repository.User_ImgRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
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
			String absolutePath = "src/main/resources/static/" + relativePath;

			try {
				Path directoryPath = Paths.get("src", "main", "resources", "static", "img", "user",
						String.valueOf(user_id));
				log.info("Directory Path: " + directoryPath);
				Files.createDirectories(directoryPath);
				Path filePath = Paths.get(absolutePath);
				Files.write(filePath, file.getBytes());
				User user = userRepository.findById(user_id)
						.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 사용자를 찾을 수 없습니다: " + user_id));
				User_Img addProfile = User_Img.builder().userId(user).original(original).path(relativePath).build();
				imgRepository.save(addProfile);
				return relativePath;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return absolutePath;
		}).collect(Collectors.toList());
	}

	// 이미지 삭제
	@Transactional
	public void imgRemove(Long user_img_id, Long user_id) {
		String relativePath = "src/main/resources/static/img/user/" + user_id;
		Path absolutePath = Paths.get(relativePath);
		try {
			// 디렉토리 체크
			Files.walk(absolutePath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
			// 디렉토리 삭제
			Files.deleteIfExists(absolutePath);
			imgRepository.deleteByUserImgId(user_img_id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
