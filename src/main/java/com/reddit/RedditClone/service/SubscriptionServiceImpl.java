package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.model.Subscription;
import com.reddit.RedditClone.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubredditService subredditService;

    @Override
    public void saveSubscription(Subscription subscription) {
        this.subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionBySubredditIdAndUserId(Long subredditId, Long userId) {
        return this.subscriptionRepository.findBySubredditIdAndUserId(subredditId, userId);
    }

    @Override
    public void removeSubscription(Long subRedditId, Long userId) {
        this.subscriptionRepository.deleteSubscription(subRedditId, userId);
    }

    @Override
    public List<Long> getSubscribedSubredditIdsByActiveUser(Long activeUserId) {
        List<Subreddit> privateSubreddits = subredditService.findAllPrivateSubreddits();
        List<Long> privateSubredditIds = new ArrayList<>();

        for(Subreddit subreddit: privateSubreddits){
            privateSubredditIds.add(subreddit.getId());
        }

        List<Subscription> subscriptions = subscriptionRepository.findBySubredditIdInAndUserId(privateSubredditIds, activeUserId);
        List<Long> subredditIds = new ArrayList<>();

        for(Subscription subscription: subscriptions) {
            if(subscription.getUserId() == activeUserId){
                subredditIds.add(subscription.getSubredditId());
                System.out.println("subscribed subreddit id: "+subscription.getSubredditId());
            }
        }
        return subredditIds;
    }

    @Override
    public boolean isUserSubscribed(Long subredditId, Long userId) {
        Subscription subscription = subscriptionRepository.findBySubredditIdAndUserId(subredditId, userId);
        if(subscription != null){
            return true;
        }
        return false;
    }
}
