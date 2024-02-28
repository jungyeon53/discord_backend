package com.imfreepass.discord.user.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.imfreepass.discord.user.api.request.AddAndRemoveProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.entity.UserImg;
import com.imfreepass.discord.user.repository.UserRepository;
import com.imfreepass.discord.user.repository.UserImgRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserImgService {

	private static final String DEFAULT_PROFILE_PATH = "src/main/resources/static/img/default_profile/";
	private static final String USER_RELATIVE_PATH = "img/user_profile/";
	private static final String USER_ABSOLUTE_PATH = "src/main/resources/static/";

	private final UserRepository userRepository;
	private final UserImgRepository imgRepository;

	/**
	 * 랜덤 기본 이미지
	 * @param user
	 * @return
	 */
	public UserImg insertRandom(Long userId) {
		Random random = new Random();
		int randomNum = random.nextInt(5)+1;
		String original = randomNum + ".jpg";
		String path = DEFAULT_PROFILE_PATH + original;
		UserImg img = UserImg.builder()
				.userId(userId)
				.original(original)
				.path(path)
				.build();
		return imgRepository.save(img);
	}

	/**
	 * 이미지 추가
	 * @param files
	 * @param userId
	 */
	public void addProfile(List<MultipartFile> files, Long userId) {
		List<String> profile = files.stream().map(file -> {
			
			String original = file.getOriginalFilename();
			// 상대경로
			String relativePath = USER_RELATIVE_PATH + userId + "/" + original;
			// 절대경로
			String absolutePath = USER_ABSOLUTE_PATH + relativePath;
			try {
				Path directoryPath = Paths.get("src", "main", "resources", "static", "img", "user_profile",
						String.valueOf(userId));
				Files.createDirectories(directoryPath);
				Path filePath = Paths.get(absolutePath);
				Files.write(filePath, file.getBytes());
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));
				UserImg addProfile = UserImg.builder().userId(user.getUserId()).original(original).path(relativePath).build();
				imgRepository.save(addProfile);
				return relativePath;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return absolutePath;
		}).collect(Collectors.toList());
	}

	/**
	 * 이미지 디렉토리 + 데이터베이스 삭제
	 * @param user_img_id
	 * @param user_id
	 */
	@Transactional
	public void imgRemove(Long user_img_id, Long user_id) {
		String relativePath = "src/main/resources/static/img/user_profile/" + user_id;
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
	
	/**
	 * 이미지 데이터베이스 삭제 
	 * @param user_img_id
	 */
	public void imgDbRemove(Long user_img_id) {
		imgRepository.deleteByUserImgId(user_img_id);
	}
	
	/**
	 * 이미지 조회 
	 * @param userId
	 * @return
	 */
	public Optional<UserImg> findUserImg(Long userId) {
		return imgRepository.findByUserId(userId);
	}

	/**
	 * 이미지 등록
	 * @param img
	 * @param userId
	 * @param files
	 */
	@Transactional
	public void registerImg(UserImg img, Long userId, List<MultipartFile> files) {
		File directory = new File(USER_ABSOLUTE_PATH + USER_RELATIVE_PATH + userId);
		// 디렉토리 안에 파일이 있는지 확인
		File[] fileDirectory = directory.listFiles();
		boolean isFileDirectoryNull = (fileDirectory == null || fileDirectory.length == 0);
		if (isFileDirectoryNull) {
			System.out.println("디렉토리에 파일이 없습니다.");
		} else {
			System.out.println("디렉토리에 파일이 있습니다.");
			Long userImgId = img.getUserImgId();
			imgRemove(userImgId, userId);
		}
		imgDbRemove(img.getUserImgId());
		addProfile(files, userId);
	}

	@Transactional
	public void defalutProfile(AddAndRemoveProfile profile) {
		imgDbRemove(profile.getUserImgId());
		imgRemove(profile.getUserImgId(), profile.getUserId());
		insertRandom(profile.getUserId());
	}
}
