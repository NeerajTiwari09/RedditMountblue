package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Vote;

import java.util.List;
import java.util.Map;

public interface VoteService {
    void saveVote(Vote vote);
    Map<Long,Vote> getVotesByPosts(List<Post> posts);
}
