package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Vote;
import com.reddit.RedditClone.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private SubredditController subredditController;

    @RequestMapping("/saveVote")
    public String saveVote(@ModelAttribute("vote") Vote vote){
        System.out.println("vote Controller: "+vote.isVote());
        voteService.saveVote(vote);
        return "sub_reddit";
    }

    @GetMapping("/vote")
    public String changeVote(@RequestParam("postId") Long postId,
                             @RequestParam("vote") boolean vote,
                             @RequestParam("subRedditId") Long subRedditId,
                             Model model){
        System.out.println("vote contribution: "+vote);

        Vote newVote = new Vote();
        newVote.setVote(vote);
        newVote.setPostId(postId);
        newVote.setUserId(1L);

        voteService.saveVote(newVote);
        return subredditController.getRedditById(subRedditId, model);
    }
}
