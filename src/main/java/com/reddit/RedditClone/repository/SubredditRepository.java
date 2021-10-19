package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    @Query("SELECT s FROM Subreddit s WHERE lower(s.name) LIKE %?1% OR " +
            "lower(s.description) LIKE %?1%")
    List<Subreddit> searchByString(String searchString);
}
