package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubredditRepository extends JpaRepository<Subreddit, Integer> {

}
