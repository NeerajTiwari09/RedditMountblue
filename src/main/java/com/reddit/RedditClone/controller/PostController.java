package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.CommunityType;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.service.PostService;
import com.reddit.RedditClone.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private SubredditService subredditService;

    @GetMapping("/createPost")
    public String viewCreatePostPage(Model model){
        List<Subreddit> subreddits = subredditService.findAllSubReddits();
        Post post = new Post();

        model.addAttribute("subreddits" ,subreddits);
        model.addAttribute("newPost" ,post);
        return "create-post";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute("newPost") Post post){
        postService.savePost(post);
        return "";
    }
}
