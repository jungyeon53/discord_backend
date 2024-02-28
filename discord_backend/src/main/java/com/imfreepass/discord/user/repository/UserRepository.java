package com.imfreepass.discord.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.user.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	@Transactional
	@Modifying
	@Query("update User u set u.refreshToken = null where u.email = :email")
	int clearTokenByEmail(@Param("email") String email);

	@Transactional
	@Modifying
	@Query("update User u set u.password = :password where u.userId = :userId")
	int updatePassword(@Param("userId") Long userId, @Param("password") String password);
	
	@Transactional
	@Modifying
	@Query("update User u set u.nickname = :nickname where u.userId = :userId")
	int updateNickname(@Param("userId") Long userId, @Param("nickname") String nickname);
	
	@Transactional
	@Modifying
	@Query("update User u set u.stateId = :stateId where u.userId = :userId")
	int updateState(@Param("userId") Long userId, @Param("stateId") int stateId);
	
	@Transactional
	@Modifying
	@Query("update User u set u.preState = :preState where u.userId = :userId")
	void updatePreState(@Param("userId") Long userId, @Param("preState") int preState);
	
	Optional<User> findByUserHash(String user_hash);
	Optional<User> findByNickname(String nickname);
	

}
