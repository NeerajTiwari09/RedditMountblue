package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.*;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class PostServiceImpl implements  PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private SubredditService subredditService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private AWSService awsService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public Post savePost(Post post) {
        Timestamp timestamp = Timestamp.from(Instant.now());
        if(!post.getImage().isEmpty()) {
            String url = awsService.uploadFile(post.getImage());
            List<Image> images = new ArrayList<>();
            Image image = new Image();
            image.setUrls(url);
            images.add(image);
            post.setImages(images);
        }
        post.setCreatedAt(timestamp);
        post.setUpdatedAt(timestamp);
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getBySubRedditId(Long id) {
        return postRepository.findAllBySubredditId(id);
    }

    @Override
    public Post getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.orElse(null);
    }

    @Override
    public void updatePostById(Post post) {
        Optional<Post> optional = postRepository.findById(post.getId());
        if(!optional.isPresent()){
            return;
        }
        Post existingPost = optional.get();
        Timestamp timestamp = Timestamp.from(Instant.now());
        if(!post.getImage().isEmpty()) {
            String url = awsService.uploadFile(post.getImage());
            List<Image> images = new ArrayList<>();
            Image image = new Image();
            image.setUrls(url);
            images.add(image);
            existingPost.setImages(images);
        }
        existingPost.setLink(post.getLink());
        existingPost.setContent(post.getContent());
        existingPost.setUpdatedAt(timestamp);
        postRepository.save(existingPost);
    }

    @Override
    public Long deleteById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()){
            postRepository.deleteById(postId);
            if(!post.get().getImages().isEmpty()) {
                String file = post.get().getImages().get(0).getUrls();
                String[] fileName = file.split("com/");
                awsService.deleteFile(fileName[1]);
            }
        }
        return post.get().getSubredditId();
    }

    @Override
    public long getKarma(Long subredditId){
        List<Post> posts = postRepository.findAllBySubredditId(subredditId);
        long karma = 0L;
        for(Post post : posts){
            karma = karma +  post.getVoteCount();
        }
        return karma;
    }

    @Override
    public List<Post> getPopularPosts(Long subredditId) {
        return postRepository.findAllPopularPostsBySubredditId(subredditId);
    }

    @Override
    public List<Post> getControversialPosts(Long subredditId) {
        return postRepository.findControversialPosts(subredditId);
    }

    @Override
    public List<Post> findAllNewPostsBySubredditId(Long subredditId) {
        return postRepository.findBySubredditIdOrderByCreatedAtDesc(subredditId);
    }

   @Override
    public SortedSet<Comment> getCommentsWithoutDuplicates(int page, Set<Long> visitedComments, SortedSet<Comment> comments) {
        page++;
        Iterator<Comment> itr = comments.iterator();
        while (itr.hasNext()) {
            Comment comment = itr.next();
            boolean addedToVisitedComments = visitedComments.add(comment.getId());
            if (!addedToVisitedComments) {
                itr.remove();
                if (page != 1)
                    return comments;
            }
            if (addedToVisitedComments && !comment.getComments().isEmpty())
                getCommentsWithoutDuplicates(page, visitedComments, comment.getComments());
        }
        return comments;
    }

    public List<Post> getSearchedPosts(String keyword) {
        if (keyword != null) {
            return postRepository.searchByString(keyword);
        }
        return postRepository.findAll();
    }

    @Override
    public List<Post> getLast24HourPosts(Long subredditId){
        return postRepository.findLast24HourPosts(subredditId);
    }

    @Override
    public List<Post> getLastWeekPosts(Long subredditId) {
        return postRepository.findLastWeekPosts(subredditId);
    }

    @Override
    public List<Post> getLastMonthPosts(Long subredditID) {
        return postRepository.findLastMonthPosts(subredditID);
    }

    @Override
    public List<Post> getLastYearPosts(Long subredditId){
        return postRepository.findLastYearPosts(subredditId);
    }

    @Override
    public String redirectToSubredditPageById(Long subredditId, List<Post> posts, Model model){
        Subreddit subreddit = subredditService.getRedditById(subredditId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);
        Long karma = getKarma(subredditId);

        Boolean isSubscribed = false;
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            user = userService.findUserByEmail(email);
            Long userId = user.getId();
            Subscription subscription = subscriptionService.getSubscriptionBySubredditIdAndUserId(subredditId, userId);
            if(subscription!=null){
                isSubscribed = true;
            }
        }

        System.out.println("Karma: " + karma);
        model.addAttribute("posts", posts);
        model.addAttribute("subReddit", subreddit);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);
        model.addAttribute("postsLength", posts.size());
        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("user", user);
        return "sub_reddit";
    }
    @Override
    public List<Post> findAllNewPosts() {
        return postRepository.findAllOrderByCreatedAtDesc();
    }

    @Override
    public List<Post> getLast24HourPosts(){
        return postRepository.findLast24HourPosts();
    }

    @Override
    public List<Post> getLastWeekPosts() {
        return postRepository.findLastWeekPosts();
    }

    @Override
    public List<Post> getLastMonthPosts() {
        return postRepository.findLastMonthPosts();
    }

    @Override
    public List<Post> getLastYearPosts(){
        return postRepository.findLastYearPosts();
    }

    @Override
    public String redirectToSubredditPage(List<Post> posts, Model model){
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);
//        Long karma = getKarma(subredditId);

        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            user = userService.findUserByEmail(email);
        }

        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);
        model.addAttribute("postsLength", posts.size());
        model.addAttribute("user", user);
        return "sub_reddit";
    }

    @Override
    public List<Post> findAllNewPostsByUssername(String author) {
        return postRepository.findByAuthor(author);
    }

    @Override
    public List<Post> findPostsBySubreddits(List<Subreddit> subreddits) {
        List<Long> subredditIds = new ArrayList<>();

        for(Subreddit subreddit: subreddits){
            subredditIds.add(subreddit.getId());
        }

        List<Post> posts = postRepository.findAllBySubredditIdIn(subredditIds);

        for(Post post: posts){
            System.out.println("posts are: "+post.getTitle());
        }
        return posts;
    }

    @Override
    public List<Post> findPostBySubredditIds(List<Long> subredditIds) {
        List<Post> posts = postRepository.findAllBySubredditIdIn(subredditIds);
        return posts;
    }
}