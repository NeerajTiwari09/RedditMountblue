package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByPostIdAndUserId(Long postId, Long userId);

}
