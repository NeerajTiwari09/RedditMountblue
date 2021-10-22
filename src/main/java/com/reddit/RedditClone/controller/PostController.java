package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.*;
import com.reddit.RedditClone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private SubredditService subredditService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/popular")
    public String popular(Model model){
        List<Post> posts = postService.findAllNewPosts();
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
//        Long karma = postService.getKarma(subredditId);
//        model.addAttribute("karma", karma);
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);

        model.addAttribute("posts", posts);
        model.addAttribute("votes", votes);
        model.addAttribute("subreddits", subreddits);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userExist", false);
        }
        else{
            model.addAttribute("userExist", true);
            String email = authentication.getName();
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }
        return "sub_reddit";
    }

    @RequestMapping("/viewProfile")
    public String viewProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userExist", false);
            return "login";
        }
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("userExist", true);

        List<Post> posts = postService.findAllNewPostsByUssername(user.getUsername());
        List<Subreddit> subreddits = subredditService.searchByUser(user.getId());
        List<Comment> comments = commentService.findByUserId(user.getId());
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);

        model.addAttribute("votes", votes);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        model.addAttribute("comments", comments);
        model.addAttribute("subreddits", subreddits);
        model.addAttribute("postsLength", posts.size());
        model.addAttribute("commentLength", comments.size());
        return "view_profile";
    }

    @GetMapping("/viewCreatePostPage")
    public String viewCreatePostPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "Please Login First to create a Post!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);

        List<Subreddit> subreddits = new ArrayList<>();
        Post post = new Post();

        subreddits.addAll(subredditService.findAllPublicSubreddits());
        subreddits.addAll(subredditService.getAllSubscribedPrivateSubreddits(user.getId()));
        subreddits.addAll(subredditService.getAllSubscribedRestrictedSubreddits(user.getId()));

        model.addAttribute("subreddits" ,subreddits);
        model.addAttribute("newPost" ,post);
        return "create-post";
    }

    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("newPost") Post post,@RequestParam("subreddit") String subredditName, Model model){
        Subreddit subreddit = subredditService.getSubredditByName(subredditName);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "Please Login First to create a Post!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }
        User user = userService.findUserByEmail(authentication.getName());
        post.setAuthor(user.getUsername());
        post.setUser(user);
        post.setSubredditId(subreddit.getId());
        postService.savePost(post);

        model.addAttribute("userExist", true);
        model.addAttribute("user", user);
        return "redirect:/reddit/"+post.getSubredditId();
    }

    @GetMapping(value = {"/","/post"})
    public String getAllPosts(Model model){
        List<Post> posts = null;
         List<Subreddit> selectedSubreddits = subredditService.findAllPublicAndRestrictedSubreddit();
        posts = postService.findPostsBySubreddits(selectedSubreddits);

        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);

        model.addAttribute("posts", posts);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userExist", false);
        }
        else{
            model.addAttribute("userExist", true);
            String email = authentication.getName();
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);

            List<Long> privateSubscribedSubredditIds = subscriptionService.getSubscribedPrivateSubredditIdsByActiveUser(user.getId());
            posts.addAll(postService.findPostBySubredditIds(privateSubscribedSubredditIds));

        }
        return "sub_reddit";
    }

    @RequestMapping("/viewPost/{postId}")
    public String viewPost(@PathVariable Long postId, Model model){
        Post post = postService.getPostById(postId);
        SortedSet<Comment> commentsWithoutDuplicates = postService.getCommentsWithoutDuplicates(0, new HashSet<Long>(), post.getComments());
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        String url = "";
        if(!post.getImages().isEmpty()) {
            url = post.getImages().get(0).getUrls();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Subreddit subreddit = subredditService.findById(post.getSubredditId());
        model.addAttribute("sub_reddit", subreddit);
        model.addAttribute("post",post);
        model.addAttribute("url", url);
        model.addAttribute("thread", commentsWithoutDuplicates);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("rootComment", new Comment());
        model.addAttribute("user", user);
        return "view_post";
    }

    @GetMapping("/update/{postId}")
    public String getUpdateViewPage(@PathVariable Long postId, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "You are not an authorised to delete this post!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }

        Post post = postService.getPostById(postId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        String imgUrl = "";
        if(!post.getImages().isEmpty()) {
            imgUrl = post.getImages().get(0).getUrls();
        }
        model.addAttribute("newPost", post);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("imgUrl", imgUrl);
        return "update-post";
    }

    @RequestMapping("/popular/{subredditId}")
    public String getPopularPostsBySubredditId(@PathVariable Long subredditId, Model model){
        Subreddit subreddit = subredditService.getRedditById(subredditId);
        List<Post> posts = postService.getPopularPosts(subredditId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(subredditId);

        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts", posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userExist", false);
        }
        else{
            model.addAttribute("userExist", true);
            String email = authentication.getName();
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }
        return "sub_reddit";
    }

    @RequestMapping("/controversial/{subredditId}")
    public String getControversialPostsBySubredditId(@PathVariable Long subredditId, Model model){
        Subreddit subreddit = subredditService.getRedditById(subredditId);
        List<Post> posts = postService.getControversialPosts(subredditId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(subredditId);

        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts", posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userExist", false);
        }
        else{
            model.addAttribute("userExist", true);
            String email = authentication.getName();
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }
        return "sub_reddit";
    }

    @RequestMapping("/popular/new/")
    public String getNewPopularPost(Model model){
        System.out.println("popular new");
        List<Post> posts = postService.findAllNewPosts();
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);
        System.out.println("Posts = "+posts);

        model.addAttribute("posts", posts);
        model.addAttribute("votes", votes);
        return "redirect:/popular/";
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute("post") Post post, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "You are not authorised to update this post!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }

        postService.updatePostById(post);
        return "redirect:/viewPost/"+post.getId();
    }

    @GetMapping("/delete/{postId}")
    public String deletePostById(@PathVariable Long postId, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "You are not authorised to delete this post!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }
        Long subredditId = postService.deleteById(postId);
        return "redirect:/reddit/"+subredditId;
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("search") String keyword,Model model) throws ParseException {
        System.out.println("this is keyword = " +keyword);
        List<Post> posts = postService.getSearchedPosts(keyword.toLowerCase());
        List<Subreddit> searchSubreddits = subredditService.getSearchedSubreddits(keyword.toLowerCase());

        Subreddit subreddit = subredditService.getRedditById(1L);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(1L);

        User user = null;
        model.addAttribute("userExist", false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            user = userService.findUserByEmail(email);
            model.addAttribute("userExist", true);
        }

        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts",posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("searchSubreddits",searchSubreddits);
        model.addAttribute("votes", votes);
        model.addAttribute("user", user);
        model.addAttribute("keyword", keyword);
        return "search";
    }

    @RequestMapping("/new/{subredditId}")
    public String getAllNewPostsBySubredditId(@PathVariable Long subredditId, Model model){
        Subreddit subreddit = subredditService.getRedditById(subredditId);
        List<Post> posts = postService.findAllNewPostsBySubredditId(subredditId);
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPageById(subredditId, posts, model);
    }

    @GetMapping("/top/t=day/{subredditId}")
    public String todayPosts(@PathVariable("subredditId") Long subredditId, Model model){
        List<Post> posts = postService.getLast24HourPosts(subredditId);
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPageById(subredditId, posts, model);
    }

    @GetMapping("/top/t=week/{subredditId}")
    public String currentWeekPosts(@PathVariable("subredditId") Long subredditId, Model model){
        List<Post> posts = postService.getLastWeekPosts(subredditId);
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPageById(subredditId, posts, model);
    }

    @GetMapping("/top/t=month/{subredditId}")
    public String currentMonthPosts(@PathVariable("subredditId") Long subredditId, Model model){
        List<Post> posts = postService.getLastMonthPosts(subredditId);
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPageById(subredditId, posts, model);
    }

    @GetMapping("/top/t=year/{subredditId}")
    public String currentYearPosts(@PathVariable("subredditId") Long subredditId, Model model){
        List<Post> posts = postService.getLastYearPosts(subredditId);
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPageById(subredditId, posts, model);
    }

    @RequestMapping("/new")
    public String getAllNewPosts(Model model){
        List<Post> posts = postService.findAllNewPosts();

        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPage(posts, model);
    }

    @GetMapping("/top/t=day")
    public String todayAllPosts(Model model){
        List<Post> posts = postService.getLast24HourPosts();

        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPage(posts, model);
    }

    @GetMapping("/top/t=week")
    public String currentWeekAllPosts(Model model){
        List<Post> posts = postService.getLastWeekPosts();
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPage(posts, model);
    }

    @GetMapping("/top/t=month")
    public String currentMonthAllPosts(Model model){
        List<Post> posts = postService.getLastMonthPosts();
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPage(posts, model);
    }

    @GetMapping("/top/t=year")
    public String currentYearAllPosts(Model model){
        List<Post> posts = postService.getLastYearPosts();
        model.addAttribute("posts", posts);
        return postService.redirectToSubredditPage(posts, model);
    }
}