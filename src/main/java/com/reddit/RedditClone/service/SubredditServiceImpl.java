package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.CommunityType;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.repository.CommunityTypeRepository;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubredditServiceImpl implements SubredditService{

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private CommunityTypeRepository communityTypeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public Subreddit saveSubreddit(Subreddit subreddit) {
        CommunityType communityType = this.communityTypeRepository.findByName(subreddit.getCommunityType().getName());
        subreddit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        subreddit.setCommunityType(communityType);
        return this.subredditRepository.save(subreddit);
    }

    @Override
    public List<Subreddit> findAllSubreddits() {
        return subredditRepository.findAll();
    }

    @Override
    public Subreddit getRedditById(Long id) {
        Optional<Subreddit> subreddit = subredditRepository.findById(id);
        return subreddit.orElse(null);
    }

    @Override
    public List<Subreddit> getSearchedSubreddits(String keyword) {
        return subredditRepository.searchByString(keyword);
    }

    @Override
    public List<Subreddit> searchByUser(Long id) {
        return subredditRepository.searchByUser(id);
    }

    @Override
    public List<Subreddit> findAllPublicAndRestrictedSubreddit() {
        List<Subreddit> subreddits = new ArrayList<>();

        subreddits.addAll(findAllPublicSubreddits());
        subreddits.addAll(findAllRestrictedSubreddits());

        return subreddits;
    }

    @Override
    public List<Subreddit> findAllPrivateSubreddits() {
        List<Subreddit> subreddits = null;
        subreddits = subredditRepository.findAllByCommunityTypeName("private");
        return subreddits;
    }

    @Override
    public List<Subreddit> findAllPublicSubreddits() {
        return subredditRepository.findAllByCommunityTypeName("public");
    }

    @Override
    public List<Subreddit> findAllRestrictedSubreddits() {
        return subredditRepository.findAllByCommunityTypeName("restricted");
    }

    @Override
    public Subreddit findById(Long subredditId) {
        return subredditRepository.getById(subredditId);
    }

    @Override
    public List<Subreddit> getAllSubscribedPrivateSubreddits(Long activeUser) {
        List<Long> subredditIds = subscriptionService.getSubscribedPrivateSubredditIdsByActiveUser(activeUser);
        List<Subreddit> subreddits = subredditRepository.findAllById(subredditIds);
        return subreddits;
    }

    @Override
    public List<Subreddit> getAllSubscribedRestrictedSubreddits(Long activeUser) {
        List<Long> subredditIds = subscriptionService.getSubscribedRestrictedSubredditIdsByActiveUser(activeUser);
        List<Subreddit> subreddits = subredditRepository.findAllById(subredditIds);
        return subreddits;
    }

    @Override
    public boolean isSubredditPrivate(Subreddit subreddit) {
        if (subreddit.getCommunityType().getName().equals("private")){
            return true;
        }
        return false;
    }

    @Override
    public Subreddit getSubredditByName(String subredditName) {
        return subredditRepository.findByName(subredditName);

    }
}
