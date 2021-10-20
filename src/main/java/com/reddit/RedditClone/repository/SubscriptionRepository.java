package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findBySubredditIdAndUserId(Long subredditId, Long userId);
}
