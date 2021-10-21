package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByPostId(Long postId);

    List<Comment> findByUserId(Long userId);

    Comment getById(Long id);

    void deleteById(Long id);

    void saveComment(Long postId, Comment rootComment, Long parentId, String childCommentText);
}
