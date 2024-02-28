package com.imfreepass.discord.user.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.user.api.request.AddAndRemoveProfile;
import com.imfreepass.discord.user.api.response.ViewUserImg;
import com.imfreepass.discord.user.entity.User;
import com.imfreepass.discord.user.entity.UserImg;

@Repository
public interface UserImgRepository extends JpaRepository<UserImg, Long>{

	void save(AddAndRemoveProfile addProfile);
	
	void deleteByUserImgId(Long user_img_id);
	
	Optional<UserImg> findByUserId(Long userId);

	

}
