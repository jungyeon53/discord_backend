package com.imfreepass.discord.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.user.api.request.AddProfile;
import com.imfreepass.discord.user.entity.User_Img;

@Repository
public interface User_ImgRepository extends JpaRepository<User_Img, Long>{

	void save(AddProfile addProfile);
	
}
