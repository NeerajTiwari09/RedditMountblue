package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.CommunityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityTypeRepository extends JpaRepository<CommunityType, Integer> {
}
