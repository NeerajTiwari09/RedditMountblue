package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByPostIdAndUserId(Long postId, Long userId);

    @Query("SELECT v FROM Vote v WHERE v.postId IN (?1)")
    List<Vote> findVotesByPostId(List<Long> postId);

}
