package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Subreddit;

import java.util.List;

public interface SubredditService {
    Subreddit saveSubreddit(Subreddit subreddit);

    List<Subreddit> findAllSubReddits();

    Subreddit getRedditById(Long id);
}
