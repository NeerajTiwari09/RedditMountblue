package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/post")
    public Post createPost(@ModelAttribute("post") Post post){
        return postService.savePost(post);
    }

}
