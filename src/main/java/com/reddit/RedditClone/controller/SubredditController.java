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

import java.util.List;
import java.util.Map;

@Controller
public class SubredditController {

    @Autowired
    private SubredditService subredditService;

    @Autowired
    private PostService postService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping("/createSubreddit")
    public String createSubreddit(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        model.addAttribute("subReddit", new Subreddit());
        return "create_subreddit";
    }

    @PostMapping("/saveSubreddit")
    public String saveSubreddit(@ModelAttribute("subReddit") Subreddit subreddit, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        subreddit.setUser(user);

        System.out.println("cc"+subreddit.getCommunityType().getName());
        Subreddit subredditResult = this.subredditService.saveSubreddit(subreddit);
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("subredditResult",subredditResult);
        model.addAttribute("posts", posts);
        return "redirect:/reddit/"+subredditResult.getId();
    }

    @GetMapping("/reddit/{id}")
    public String getRedditById(@PathVariable Long id, Model model){
        Subreddit subreddit = subredditService.getRedditById(id);
        List<Post> posts = postService.getBySubRedditId(id);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long,Vote> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(id);

        Boolean isSubscribed = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User user = userService.findUserByEmail(email);
            Long userId = user.getId();
            Subscription subscription = subscriptionService.getSubscriptionBySubredditIdAndUserId(id, userId);
            if(subscription!=null){
                isSubscribed = true;
            }
        }

        System.out.println("Karma: " + karma);
        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts", posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits", subreddits);
        model.addAttribute("votes", votes);
        model.addAttribute("postsLength", posts.size());
        model.addAttribute("isSubscribed", isSubscribed);
//        model.addAttribute("currentUserId", 1);
        postService.getControversialPosts(id);

        return "sub_reddit";
    }

    @RequestMapping("/subscribe")
    public String subscribe(@RequestParam(name = "id") Long subRedditId, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        System.out.println("subRedditId "+subRedditId);
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Long userId = user.getId();
        System.out.println("userId "+userId);

        Subscription subscription = new Subscription();
        subscription.setSubredditId(subRedditId);
        subscription.setUserId(userId);

        Subscription subscriptionResult = subscriptionService.saveSubscription(subscription);

        return "redirect:/reddit/"+subRedditId;
    }
}