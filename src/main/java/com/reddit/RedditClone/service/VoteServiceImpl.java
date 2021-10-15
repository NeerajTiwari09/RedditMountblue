package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Vote;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.UserRepository;
import com.reddit.RedditClone.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveVote(Vote vote) {
        Post post = postRepository.getById(vote.getPostId());
        User user = userRepository.findByUsername("Shreya");
        vote.setUserId(user.getId());
        Vote voteByPostIdAndUserId = voteRepository.findByPostIdAndUserId(post.getId(), user.getId());
        if (voteByPostIdAndUserId != null && voteByPostIdAndUserId.isVote() == vote.isVote()) {
            voteRepository.delete(voteByPostIdAndUserId);
            // if else
        }else if(voteByPostIdAndUserId != null && voteByPostIdAndUserId.isVote() != vote.isVote()){
            vote.setId(voteByPostIdAndUserId.getId());
//            if else
            voteRepository.save(vote);
        }else {
//            if else
            voteRepository.save(vote);
        }
        postRepository.save(post);
    }

}
