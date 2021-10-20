package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findBySubredditIdAndUserId(Long subredditId, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Subscription sub WHERE sub.subredditId = ?1 and sub.userId = ?2")
    void deleteSubscription(Long subRedditId, Long userId);
}
