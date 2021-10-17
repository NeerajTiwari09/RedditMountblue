package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.model.Post;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

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

    SortedSet<Comment> getCommentsWithoutDuplicates(int i, Set<Long> longs, SortedSet<Comment> comments);
}
