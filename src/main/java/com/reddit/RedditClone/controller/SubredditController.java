package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubredditController {
    @Autowired
    private SubredditService subredditService;

    @RequestMapping("/createSubreddit")
    public String createSubreddit(){
        return "create_subreddit";
    }

    @PostMapping("/saveSubreddit")
    public Subreddit saveSubreddit(@RequestBody Subreddit subreddit){
        System.out.println("subreddit "+subreddit);
        Subreddit subredditResult = this.subredditService.saveSubreddit(subreddit);
        return subreddit;
    }
}
