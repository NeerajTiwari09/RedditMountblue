package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.model.Vote;
import com.reddit.RedditClone.repository.UserRepository;
import com.reddit.RedditClone.service.PostService;
import com.reddit.RedditClone.service.UserService;
import com.reddit.RedditClone.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private SubredditController subredditController;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostController postController;

    @Deprecated
    @RequestMapping("/saveVote")
    public String saveVote(@ModelAttribute("vote") Vote vote, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);

        voteService.saveVote(vote);

        model.addAttribute("userId", user.getId());
        return "sub_reddit";
    }

    @GetMapping("/vote")
    public String changeVote(@RequestParam("postId") Long postId,
                             @RequestParam(required = false, name = "upVote", defaultValue = "false") boolean upVote,
                             @RequestParam(required = false, name = "downVote", defaultValue = "false") boolean downVote,
                             @RequestParam(required = false, name = "isHomePage", defaultValue = "false") boolean isHomePage,
                             @RequestParam(required = false, name = "isProfile", defaultValue = "false") boolean isProfile,
                             @RequestParam(required = false, name = "isSearch", defaultValue = "false") boolean isSearch,
                             @RequestParam(required = false, name = "keyword", defaultValue = "") String keyword,
                             Model model) throws ParseException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "Sorry!! Please Login First!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }
        System.out.println("change Vote controller");
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);

        Vote newVote = new Vote();
        newVote.setUpVoted(upVote);
        newVote.setDownVoted(downVote);
        newVote.setPostId(postId);
        newVote.setUserId(user.getId());

        voteService.saveVote(newVote);

        Post post = postService.getPostById(postId);

        if(isHomePage){
            return subredditController.getRedditById(post.getSubredditId(), model);
        }

        if (isProfile){
            return postController.viewProfile(model);
        }

        if(isSearch){
            return postController.searchPosts(keyword, model);
        }

        return postController.popular(model);
    }
}
