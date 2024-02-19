package com.imfreepass.discord.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imfreepass.discord.user.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long>{

}
