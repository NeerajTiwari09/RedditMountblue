package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @RequestMapping("/deleteComment/{id}")
    private String deleteComment(@PathVariable Long id){
        Comment comment = commentService.getById(id);
        commentService.deleteById(id);
        return "redirect:/viewPost/" + comment.getPost().getId();
    }
}
