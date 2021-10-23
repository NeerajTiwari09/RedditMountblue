package com.reddit.RedditClone.service;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.reddit.RedditClone.model.Vote;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.UserRepository;
import com.reddit.RedditClone.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        if (voteByPostIdAndUserId != null) {
            if (!vote.isUpVoted() && !vote.isDownVoted()) {
                System.out.println("resetting the contribution");
                if (voteByPostIdAndUserId.isUpVoted()) {
                    post.setUpVoteCount(post.getUpVoteCount() - 1);
                    post.setVoteCount(post.getVoteCount() - 1);
                }
                if (voteByPostIdAndUserId.isDownVoted()) {
                    post.setDownVoteCount(post.getDownVoteCount() - 1);
                    post.setVoteCount(post.getVoteCount() + 1);
                }
                voteRepository.delete(voteByPostIdAndUserId);
            }
            else if (vote.isUpVoted() && voteByPostIdAndUserId.isDownVoted()) {
                post.setUpVoteCount(post.getUpVoteCount() + 1);
                post.setDownVoteCount(post.getDownVoteCount() - 1);
                post.setVoteCount(post.getVoteCount() + 2);

                voteByPostIdAndUserId.setDownVoted(!voteByPostIdAndUserId.isDownVoted());
                voteByPostIdAndUserId.setUpVoted(!voteByPostIdAndUserId.isUpVoted());

                voteRepository.save(voteByPostIdAndUserId);
            } else if (vote.isDownVoted() && voteByPostIdAndUserId.isUpVoted()) {
                post.setUpVoteCount(post.getUpVoteCount() - 1);
                post.setDownVoteCount(post.getDownVoteCount() + 1);
                post.setVoteCount(post.getVoteCount() - 2);

                voteByPostIdAndUserId.setDownVoted(!voteByPostIdAndUserId.isDownVoted());
                voteByPostIdAndUserId.setUpVoted(!voteByPostIdAndUserId.isUpVoted());

                voteRepository.save(voteByPostIdAndUserId);
            }
        }
            else {
                System.out.println("Contributing First time");
                vote.setContributed(true);
                if (vote.isUpVoted()) {
                    post.setUpVoteCount(post.getUpVoteCount() + 1);
                    post.setVoteCount(post.getVoteCount() + 1);
                } else if (vote.isDownVoted()) {
                    post.setVoteCount(post.getVoteCount() - 1);
                    post.setDownVoteCount(post.getDownVoteCount() + 1);
                }
                voteRepository.save(vote);
            }
            postRepository.save(post);
        }

    @Override
    public Map<Long,Vote> getVotesByPosts(List<Post> posts) {
        List<Long> postIds = new ArrayList<>();
        for(Post post: posts){
            postIds.add(post.getId());
        }
        List<Vote> votes = voteRepository.findVotesByPostId(postIds);
        Map<Long, Vote> votesMap = new HashMap<>();

        for(Vote vote: votes) {
            votesMap.put(vote.getPostId(), vote);
        }
        return votesMap;
    }
}