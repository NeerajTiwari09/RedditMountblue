package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Subreddit;

import java.util.List;

public interface SubredditService {
    Subreddit saveSubreddit(Subreddit subreddit);

    List<Subreddit> findAllSubreddits();

    Subreddit getRedditById(Long id);

    List<Subreddit> getSearchedSubreddits(String keyword);

    List<Subreddit> searchByUser(Long id);

    List<Subreddit> findAllPublicAndRestrictedSubreddit();

    List<Subreddit> findAllPrivateSubreddits();
}
