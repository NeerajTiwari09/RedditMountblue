package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.CommunityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityTypeRepository extends JpaRepository<CommunityType, Integer> {

    CommunityType findByName(String s);
}
