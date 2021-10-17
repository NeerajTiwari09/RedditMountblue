package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Post;

import java.util.List;

public interface PostService {

    Post savePost(Post post);

    List<Post> getAllPosts();

    List<Post> getBySubRedditId(Long id);

    Post getPostById(Long postId);

    void updatePostById(Post post);

    Long deleteById(Long postId);

    long getKarma(Long subredditId);

    List<Post> getPopularPosts(Long subredditId);

    List<Post> findAllNewPostsBySubredditId(Long subredditId);
}
