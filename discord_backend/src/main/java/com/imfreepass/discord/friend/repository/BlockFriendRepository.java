package com.imfreepass.discord.friend.repository;

import com.imfreepass.discord.friend.api.response.ViewBlockUserList;
import com.imfreepass.discord.friend.entity.BlockFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockFriendRepository extends JpaRepository<BlockFriend, Long> {
    List<BlockFriend> findBySendUserId(Long sendUserId);
}
