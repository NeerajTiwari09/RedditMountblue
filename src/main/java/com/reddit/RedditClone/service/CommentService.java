package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByPostId(Long postId);

    Comment getById(Long id);

    void deleteById(Long id);
}
