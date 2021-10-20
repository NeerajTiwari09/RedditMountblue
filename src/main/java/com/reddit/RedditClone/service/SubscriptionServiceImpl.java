package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Subscription;
import com.reddit.RedditClone.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionBySubredditIdAndUserId(Long subredditId, Long userId) {
        return subscriptionRepository.findBySubredditIdAndUserId(subredditId, userId);
    }
}
