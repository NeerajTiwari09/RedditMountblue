package com.reddit.RedditClone.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.reddit.RedditClone.model.Subscription;

public interface SubscriptionService {
    Subscription saveSubscription(Subscription subscription);

    Subscription getSubscriptionBySubredditIdAndUserId(Long subredditId, Long userId);
}
