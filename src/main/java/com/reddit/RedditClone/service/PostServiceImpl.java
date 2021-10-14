package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Image;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class PostServiceImpl implements  PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Override
    public Post savePost(Post post) {
        Timestamp timestamp = Timestamp.from(Instant.now());
        Optional<Subreddit> subreddit = subredditRepository.findById(post.getSubredditId());
        post.setCreatedAt(timestamp);
        post.setUpdatedAt(timestamp);
        post.setSubReddit(subreddit.get());
        return postRepository.save(post);
    }
}
