package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.model.CommunityType;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.service.PostService;
import com.reddit.RedditClone.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private SubredditService subredditService;

    @GetMapping("/viewCreatePostPage")
    public String viewCreatePostPage(Model model){
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Post post = new Post();

        model.addAttribute("subreddits" ,subreddits);
        model.addAttribute("newPost" ,post);
        return "create-post";
    }

    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("newPost") Post post){
        System.out.println(post.getImage());
        postService.savePost(post);
        return "redirect:/reddit/"+post.getSubredditId();
    }

    @GetMapping("/post")
    public String getAllPosts(Model model){
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        System.out.println("posts"+posts);
        return "sub_reddit";
    }

    @RequestMapping("/viewPost/{postId}")
    public String viewPost(@PathVariable Long postId, Model model){
        Post post = postService.getPostById(postId);
        SortedSet<Comment> commentsWithoutDuplicates =postService.getCommentsWithoutDuplicates(0, new HashSet<Long>(), post.getComments());

        String url = post.getImages().get(0).getUrls();
        model.addAttribute("post",post);
        model.addAttribute("url", url);
        model.addAttribute("thread", commentsWithoutDuplicates);
        model.addAttribute("rootComment", new Comment());
        return "view_post";
    }

    @GetMapping("/update/{postId}")
    public String getUpdateViewPage(@PathVariable Long postId, Model model){
        Post post = postService.getPostById(postId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        String imgUrl = post.getImages().get(0).getUrls();
        model.addAttribute("newPost", post);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("imgUrl", imgUrl);
        return "create-post";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("post") Post post){
        postService.updatePostById(post);
        return "my_homepage";
    }

    @GetMapping("/delete/{postId}")
    public String deletePostById(@PathVariable Long postId){
        Long subredditId = postService.deleteById(postId);
        return "redirect:/reddit/"+subredditId;
    }
}
