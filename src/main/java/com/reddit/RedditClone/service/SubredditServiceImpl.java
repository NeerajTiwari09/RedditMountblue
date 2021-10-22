package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.CommunityType;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.repository.CommunityTypeRepository;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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

    @Override
    public Subreddit saveSubreddit(Subreddit subreddit) {
        CommunityType communityType = this.communityTypeRepository.findByName(subreddit.getCommunityType().getName());
        subreddit.setCreatedAt(new Date(System.currentTimeMillis()));
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
        List<Subreddit> publicSubreddits = subredditRepository.findAllByCommunityTypeName("public");
        List<Subreddit> restrictedSubreddits = subredditRepository.findAllByCommunityTypeName("restricted");

        subreddits.addAll(publicSubreddits);
        subreddits.addAll(restrictedSubreddits);

        for(Subreddit publicSubreddit : publicSubreddits){
            System.out.println("public Subreddits: "+publicSubreddit.getName());
        }

        for(Subreddit publicSubreddit : restrictedSubreddits){
            System.out.println("restricted Subreddits: "+publicSubreddit.getName());
        }

        return subreddits;
    }

    @Override
    public List<Subreddit> findAllPrivateSubreddits() {
        List<Subreddit> subreddits = null;
        subreddits = subredditRepository.findAllByCommunityTypeName("private");
        return subreddits;
    }
}
