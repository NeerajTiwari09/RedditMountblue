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

    List<Subreddit> getAllSubscribedPrivateSubreddits(Long activeUser);

    List<Subreddit> getAllSubscribedRestrictedSubreddits(Long activeUser);

    boolean isSubredditPrivate(Subreddit subreddit);

    Subreddit getSubredditByName(String subredditName);

    List<Subreddit> findAllPublicSubreddits();

    List<Subreddit> findAllRestrictedSubreddits();

    Subreddit findById(Long subredditId);
}
