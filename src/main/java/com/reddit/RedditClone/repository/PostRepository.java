package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubredditId(Long id);

    @Query("SELECT pp FROM Post pp WHERE pp.subredditId= ?1 order by pp.upVoteCount desc")
    List<Post> findAllPopularPostsBySubredditId(Long subredditId);

    List<Post> findBySubredditIdOrderByCreatedAtDesc(Long subredditId);
}
