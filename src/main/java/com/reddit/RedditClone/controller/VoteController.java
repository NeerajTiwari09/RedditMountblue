package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Vote;
import com.reddit.RedditClone.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

public class VoteController {

    @Autowired
    private VoteService voteService;

    @RequestMapping("/saveVote")
    public String saveVote(@ModelAttribute("vote") Vote vote){
        voteService.saveVote(vote);
        return "";
    }
}
