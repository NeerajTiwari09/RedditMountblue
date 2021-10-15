package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.CommunityType;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.service.PostService;
import com.reddit.RedditClone.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubredditController {

    @Autowired
    private SubredditService subredditService;

    @Autowired
    private PostService postService;

    @RequestMapping("/createSubreddit")
    public String createSubreddit(Model model){
        model.addAttribute("subReddit", new Subreddit());
        return "create_subreddit";
    }

    @PostMapping("/saveSubreddit")
    public String saveSubreddit(@ModelAttribute("subReddit") Subreddit subreddit){
        System.out.println(subreddit.getCommunityType().getName());
        Subreddit subredditResult = this.subredditService.saveSubreddit(subreddit);
        return "my-homepage";
    }

    @GetMapping("/reddit/{id}")
    public Subreddit getRedditById(@PathVariable Long id, Model model){
        Subreddit subreddit = subredditService.getRedditById(id);
        List<Post> posts = postService.getBySubReditId(id);
        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts", posts);
        subreddit.setPosts(posts);
        return subreddit;
    }
}