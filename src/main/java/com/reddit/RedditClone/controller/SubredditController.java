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
            String errMsg = "Please Login First to create a Community!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }
        model.addAttribute("subReddit", new Subreddit());
        model.addAttribute("isPublic", true);
        return "create_subreddit";
    }

    @PostMapping("/saveSubreddit")
    public String saveSubreddit(@ModelAttribute("subReddit") Subreddit subreddit, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "Please Login First to create a Community!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }

        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        subreddit.setUser(user);
        Subreddit subredditResult ;

        String communityName = subreddit.getName();
        if(!communityName.startsWith("r/")){
            subreddit.setName("r/" + communityName);
        }
        if(subreddit.getCommunityType() == null){
            String errMsg = "Please provide a community type!!";
            model.addAttribute("errCommunityType", errMsg);
            return "create_subreddit";
        }
        try{
            subredditResult = this.subredditService.saveSubreddit(subreddit);
        } catch(Exception ex){
            String errMsg = "Please provide a unique community name!!";
            model.addAttribute("errMsg", errMsg);
            return "create_subreddit";
        }

        List<Post> posts = postService.getAllPosts();

        model.addAttribute("subredditResult",subredditResult);
        model.addAttribute("posts", posts);
        return "redirect:/reddit/"+subredditResult.getId();
    }

    @GetMapping("/reddit/{id}")
    public String getRedditById(@PathVariable Long id, Model model){
        Boolean isSubscribed = false;
        User user = null;
        List<Post> posts = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("userExist", false);
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            user = userService.findUserByEmail(email);
            Long userId = user.getId();
            Subscription subscription = subscriptionService.getSubscriptionBySubredditIdAndUserId(id, userId);
            if(subscription!=null){
                isSubscribed = true;
            }
            model.addAttribute("userExist", true);
        }

        Subreddit subreddit = subredditService.getRedditById(id);

        //if sub-reddit is private and the current user is subscribed to it then only fetch it.
        if (subredditService.isSubredditPrivate(subreddit)
                && user!= null
                && subscriptionService.isUserSubscribed(subreddit.getId(), user.getId())){

            posts = postService.getBySubRedditId(id);
            System.out.println("private comm posts are overriding posts...");
        }
        //if sub-reddit is not private
        if(!subredditService.isSubredditPrivate(subreddit)) {
            posts = postService.getBySubRedditId(id);
        }

        if(posts != null){
            Map<Long, Map<Long, Vote>> votes = voteService.getVotesByPosts(posts);
            model.addAttribute("votes", votes);
            model.addAttribute("postsLength", posts.size());
            model.addAttribute("posts", posts);
        }


        List<Subreddit> subreddits = subredditService.findAllSubreddits();


        Long karma = postService.getKarma(id);

        System.out.println("Karma: " + karma);
        model.addAttribute("subReddit", subreddit);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits", subreddits);
        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("user", user);
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

        Subscription subscription = new Subscription();
        subscription.setSubredditId(subRedditId);
        subscription.setUserId(userId);
        subscriptionService.saveSubscription(subscription);
        return "redirect:/reddit/"+subRedditId;
    }

    @RequestMapping("/unsubscribe")
    public String unsubscribe(@RequestParam("id") Long subRedditId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        System.out.println("subRedditId "+subRedditId);

        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Long userId = user.getId();
        subscriptionService.removeSubscription(subRedditId, userId);
        return "redirect:/reddit/"+subRedditId;
    }
}