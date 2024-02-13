package com.imfreepass.discord.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
	@Transactional
    @Modifying
    @Query("update User u SET u.refreshToken = null where u.email = :email")
    void clearTokenByEmail(@Param("email") String email);
}
