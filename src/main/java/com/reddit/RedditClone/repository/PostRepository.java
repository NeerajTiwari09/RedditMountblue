package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubredditId(Long id);

    @Query("SELECT p FROM Post p WHERE lower(p.title) LIKE %?1% OR " +
            "lower(p.author) LIKE %?1% OR lower(p.content) LIKE %?1%")
    List<Post> searchByString(String searchString);


    @Query("SELECT pp FROM Post pp WHERE pp.subredditId= ?1 order by pp.upVoteCount desc")
    List<Post> findAllPopularPostsBySubredditId(Long subredditId);

    List<Post> findBySubredditIdOrderByCreatedAtDesc(Long subredditId);

    List<Post> findBySubredditIdOrderByVoteCount(Long subredditId);

    @Query("SELECT p FROM Post p WHERE p.subredditId = ?1 ORDER BY p.voteCount ASC, p.upVoteCount DESC, p.downVoteCount DESC")
    List<Post> findControversialPosts(Long subredditId);

    @Query(value = "SELECT * FROM posts WHERE subreddit_id = ?1 AND created_at BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() ORDER BY created_at DESC", nativeQuery = true)
    List<Post> findLast24HourPosts(Long subredditId);

    @Query(value = "SELECT * FROM posts \n" +
            "WHERE subreddit_id = ?1 AND created_at BETWEEN NOW() - INTERVAL '1 WEEK' AND NOW() \n" +
            "ORDER BY created_at DESC;", nativeQuery = true)
    List<Post> findLastWeekPosts(Long subredditId);

    @Query(value = "SELECT * FROM posts \n" +
            "WHERE subreddit_id = ?1 AND created_at BETWEEN NOW() - INTERVAL '1 MONTH' AND NOW() \n" +
            "ORDER BY created_at DESC;", nativeQuery = true)
    List<Post> findLastMonthPosts(Long subredditId);

    @Query(value = "SELECT * FROM posts \n" +
            "WHERE subreddit_id = ?1 AND created_at BETWEEN NOW() - INTERVAL '1 YEAR' AND NOW() \n" +
            "ORDER BY created_at DESC;", nativeQuery = true)
    List<Post> findLastYearPosts(Long subredditId);

    @Query("SELECT p FROM Post p WHERE p.upVoteCount > 10 AND p.downVoteCount > 10 ORDER BY p.voteCount ASC, p.upVoteCount DESC, p.downVoteCount DESC")
    List<Post> findControversialPosts();

    @Query(value = "SELECT * FROM posts WHERE created_at BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() ORDER BY created_at DESC", nativeQuery = true)
    List<Post> findLast24HourPosts();

    @Query(value = "SELECT * FROM posts \n" +
            "WHERE created_at BETWEEN NOW() - INTERVAL '1 WEEK' AND NOW() \n" +
            "ORDER BY created_at DESC;", nativeQuery = true)
    List<Post> findLastWeekPosts();

    @Query(value = "SELECT * FROM posts \n" +
            "WHERE created_at BETWEEN NOW() - INTERVAL '1 MONTH' AND NOW() \n" +
            "ORDER BY created_at DESC;", nativeQuery = true)
    List<Post> findLastMonthPosts();

    @Query(value = "SELECT * FROM posts \n" +
            "WHERE created_at BETWEEN NOW() - INTERVAL '1 YEAR' AND NOW() \n" +
            "ORDER BY created_at DESC;", nativeQuery = true)
    List<Post> findLastYearPosts();

    @Query("SELECT pp FROM Post pp order by pp.createdAt desc")
    List<Post> findAllOrderByCreatedAtDesc();

    List<Post> findByAuthor(String author);

    List<Post> findAllBySubredditIdIn(List<Long> subredditIds);
}
