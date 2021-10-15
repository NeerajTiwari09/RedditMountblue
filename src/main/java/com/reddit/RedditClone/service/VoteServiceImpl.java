package com.reddit.RedditClone.service;

import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl {
    @Override
    public void saveVote(Vote vote) {
        Post post = postRepository.getById(vote.getPostId());
        User user = userRepository.findByUsername("Shreya");
        vote.setUserId(user.getId());
        Vote voteByPostIdAndUserId = voteRepository.findByPostIdAndUserId(post.getId(), user.getId());
        if (voteByPostIdAndUserId != null && voteByPostIdAndUserId.isVote() == vote.isVote()) {
            voteRepository.delete(voteByPostIdAndUserId);

        }else if(voteByPostIdAndUserId != null && voteByPostIdAndUserId.isVote() != vote.isVote()){
            vote.setId(voteByPostIdAndUserId.getId());

            voteRepository.save(vote);
        }else {

            voteRepository.save(vote);
        }
        postRepository.save(post);
    }

}
