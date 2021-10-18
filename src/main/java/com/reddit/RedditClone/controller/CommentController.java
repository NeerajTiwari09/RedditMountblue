package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    public CommentService commentService;

    @Autowired
    public PostRepository postRepo;


    @ResponseBody
    public List<Comment> getComments (@PathVariable Long postId) {
        List<Comment> findByPostId = commentService.findByPostId(postId);
        return findByPostId;
    }

    @PostMapping("/saveComment/{postId}/comments")
    public String postComment(@PathVariable Long postId, Comment rootComment,
                              @RequestParam(required=false) Long parentId,
                              @RequestParam(required=false) String childCommentText) {
        commentService.saveComment(postId, rootComment, parentId, childCommentText);

        return "redirect:/viewPost/" + postId;
    }

   
}
