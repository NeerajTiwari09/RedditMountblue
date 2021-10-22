package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.ui.Model;

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

    List<Post> getControversialPosts(Long subredditId);

    List<Post> findAllNewPostsBySubredditId(Long subredditId);

    SortedSet<Comment> getCommentsWithoutDuplicates(int i, Set<Long> longs, SortedSet<Comment> comments);

    List<Post> getSearchedPosts(String search);

    List<Post> findAllNewPosts();

    List<Post> getLast24HourPosts(Long subredditId);

    List<Post> getLastWeekPosts(Long subredditId);

    List<Post> getLastMonthPosts(Long subredditID);

    List<Post> getLastYearPosts(Long subredditId);

    String redirectToSubredditPageById(Long subredditId, List<Post> posts , Model model);

    List<Post> getLast24HourPosts();

    List<Post> getLastWeekPosts();

    List<Post> getLastMonthPosts();

    List<Post> getLastYearPosts();

    String redirectToSubredditPage(List<Post> posts , Model model);

    List<Post> findAllNewPostsByUssername(String author);

    List<Post> findPostsBySubreddits(List<Subreddit> subreddits);


}
