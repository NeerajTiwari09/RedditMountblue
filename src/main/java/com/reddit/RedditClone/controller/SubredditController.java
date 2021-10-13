package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Controller
public class SubredditController {
    @Autowired
    private SubredditService subredditService;

    @RequestMapping("/createSubreddit")
    public String createSubreddit(){
        return "create_subreddit";
    }

    @PostMapping("/saveSubreddit")
    public String saveSubreddit(@RequestBody Subreddit subreddit){
        Subreddit subredditResult = this.subredditService.saveSubreddit(subreddit);
        System.out.println("subredditResult "+subredditResult);
        return "redirect:/create_subreddit";
    }
}
