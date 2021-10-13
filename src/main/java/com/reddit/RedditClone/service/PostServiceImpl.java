package com.reddit.RedditClone.service;

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

        post.setCreatedAt(timestamp);
        post.setUpdatedAt(timestamp);

        Optional<Subreddit> subReddit = subredditRepository.findById(1L);
        if(subReddit.isPresent()){
            post.getSubRedditId().add(subReddit.get());
//            System.out.println(subReddit.get());
        }

        return postRepository.save(post);
    }
}
