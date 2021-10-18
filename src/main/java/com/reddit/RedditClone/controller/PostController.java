package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.*;
import com.reddit.RedditClone.service.PostService;
import com.reddit.RedditClone.service.SubredditService;
import com.reddit.RedditClone.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private SubredditService subredditService;

    @GetMapping("/popular")
    public String popular(Model model){
        List<Post> posts = new ArrayList<>();
        List<Subreddit> subreddits = subredditService.findAllSubreddits();

        model.addAttribute("posts", posts);
        model.addAttribute("subreddits", subreddits);
        return "popular";
    }

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
        SortedSet<Comment> commentsWithoutDuplicates = postService.getCommentsWithoutDuplicates(0, new HashSet<Long>(), post.getComments());
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        String url ="";
        if(!post.getImages().isEmpty()) {
            url = post.getImages().get(0).getUrls();
        }
        model.addAttribute("post",post);
        model.addAttribute("url", url);
        model.addAttribute("thread", commentsWithoutDuplicates);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("rootComment", new Comment());
        return "view_post";
    }

    @GetMapping("/update/{postId}")
    public String getUpdateViewPage(@PathVariable Long postId, Model model){
        Post post = postService.getPostById(postId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        String imgUrl = "";
        if(!(post.getImage() == null)) {
            imgUrl = post.getImages().get(0).getUrls();
        }
        model.addAttribute("newPost", post);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("imgUrl", imgUrl);
        return "update-post";
    }

    @RequestMapping("/popular/{subredditId}")
    public String getPopularPostsBySubredditId(@PathVariable Long subredditId, Model model){
        Subreddit subreddit = subredditService.getRedditById(subredditId);
        List<Post> posts = postService.getPopularPosts(subredditId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Vote> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(subredditId);

        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts", posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);
        return "sub_reddit";
    }

    @RequestMapping("/controversial/{subredditId}")
    public String getControversialPostsBySubredditId(@PathVariable Long subredditId, Model model){
        Subreddit subreddit = subredditService.getRedditById(subredditId);
        List<Post> posts = postService.getControversialPosts(subredditId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Vote> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(subredditId);

        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts", posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);
        return "sub_reddit";
    }

    @RequestMapping("/new/{subredditId}")
    public String getAllNewPostsBySubredditId(@PathVariable Long subredditId, Model model){
        Subreddit subreddit = subredditService.getRedditById(subredditId);
        List<Post> posts = postService.findAllNewPostsBySubredditId(subredditId);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Vote> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(subredditId);

        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts", posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);
        return "sub_reddit";
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute("post") Post post){
        postService.updatePostById(post);
        return "redirect:/viewPost/"+post.getId();
    }

    @GetMapping("/delete/{postId}")
    public String deletePostById(@PathVariable Long postId){
        Long subredditId = postService.deleteById(postId);
        return "redirect:/reddit/"+subredditId;
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("search") String keyword,Model model) throws ParseException {
        List<Post> posts = postService.getSearchedPosts(keyword.toLowerCase());
        Subreddit subreddit = subredditService.getRedditById(1L);
        List<Subreddit> subreddits = subredditService.findAllSubreddits();
        Map<Long, Vote> votes = voteService.getVotesByPosts(posts);
        Long karma = postService.getKarma(1L);

        model.addAttribute("subReddit", subreddit);
        model.addAttribute("posts",posts);
        model.addAttribute("karma", karma);
        model.addAttribute("subreddits",subreddits);
        model.addAttribute("votes", votes);
        return "sub_reddit";
    }

}
