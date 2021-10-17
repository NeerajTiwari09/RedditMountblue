package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.getById(id);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
