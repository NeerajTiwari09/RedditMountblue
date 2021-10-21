package com.reddit.RedditClone.service;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.reddit.RedditClone.model.Vote;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.UserRepository;
import com.reddit.RedditClone.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  UserService userService;

    @Override
    public void saveVote(Vote vote) {
        User user = null;
        Post post = postRepository.getById(vote.getPostId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Optional<User> optionalUser = userRepository.findById(vote.getUserId());
        System.out.println("Saving vote");

        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }
        if(user == null){
            System.out.println("User not found!!");
            return;
        }

        vote.setUserId(user.getId());
        Vote voteByPostIdAndUserId = voteRepository.findByPostIdAndUserId(post.getId(), user.getId());

        //if user has already contributed but resetting the contribution.
        if (voteByPostIdAndUserId != null) {
            //resetting the contribution
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
//                vote.setContributed(false);
                voteRepository.delete(voteByPostIdAndUserId);
            }//reversing the contribution if already contributed
            else if (vote.isUpVoted() && voteByPostIdAndUserId.isDownVoted()) {
                //reversing downvote to upvote
                System.out.println("reversing the vote");
                post.setUpVoteCount(post.getUpVoteCount() + 1);
                post.setDownVoteCount(post.getDownVoteCount() - 1);
                post.setVoteCount(post.getVoteCount() + 2);

                voteByPostIdAndUserId.setDownVoted(!voteByPostIdAndUserId.isDownVoted());
                voteByPostIdAndUserId.setUpVoted(!voteByPostIdAndUserId.isUpVoted());

                voteRepository.save(voteByPostIdAndUserId);
            } else if (vote.isDownVoted() && voteByPostIdAndUserId.isUpVoted()) {
                //reversing upvote to downvote
                System.out.println("reversing the vote");
                post.setUpVoteCount(post.getUpVoteCount() - 1);
                post.setDownVoteCount(post.getDownVoteCount() + 1);
                post.setVoteCount(post.getVoteCount() - 2);

                voteByPostIdAndUserId.setDownVoted(!voteByPostIdAndUserId.isDownVoted());
                voteByPostIdAndUserId.setUpVoted(!voteByPostIdAndUserId.isUpVoted());

                voteRepository.save(voteByPostIdAndUserId);
            }
        }//contributing first time
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
    public Map<Long, Map<Long, Vote>> getVotesByPosts(List<Post> posts) {
        List<Long> postIds = new ArrayList<>();
        for(Post post: posts){
            postIds.add(post.getId());
        }
        List<Vote> votes = voteRepository.findVotesByPostId(postIds);
        Map<Long, Map<Long, Vote>> votesPostMap = new HashMap<>();

        for(Vote vote: votes) {
            Map<Long, Vote> votesUserMap = new HashMap<>();

            votesUserMap.put(vote.getUserId(), vote);
            votesPostMap.put(vote.getPostId(), votesUserMap);
        }
        return votesPostMap;
    }
}