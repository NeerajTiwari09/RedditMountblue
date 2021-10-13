package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.CommunityType;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.repository.CommunityTypeRepository;
import com.reddit.RedditClone.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class SubredditServiceImp implements SubredditService{
    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private CommunityTypeRepository communityTypeRepository;

    @Override
    public Subreddit saveSubreddit(Subreddit subreddit) {
        CommunityType communityType = this.communityTypeRepository.findByName(subreddit.getCommunityType().getName());
        System.out.println("CommunityType = "+communityType.getName());
        subreddit.setCommunityType(communityType);
        return this.subredditRepository.save(subreddit);
    }

}
