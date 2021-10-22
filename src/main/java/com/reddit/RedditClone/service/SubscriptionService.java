package com.reddit.RedditClone.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.model.Subscription;

import java.util.List;

public interface SubscriptionService {
    void saveSubscription(Subscription subscription);

    Subscription getSubscriptionBySubredditIdAndUserId(Long subredditId, Long userId);

    void removeSubscription(Long subRedditId, Long userId);

    List<Long> getSubscribedSubredditIdsByActiveUser(Long activeUserId);
}
