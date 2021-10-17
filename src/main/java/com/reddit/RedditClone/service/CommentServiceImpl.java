package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.repository.CommentRepository;
import com.reddit.RedditClone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

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

    @Override
    public void saveComment(Long postId, Comment rootComment, Long parentId, String childCommentText) {
        Optional<Post> postOpt = postRepository.findById(postId);
        // save a root level comment here
        if (!StringUtils.isEmpty(rootComment.getText())) {
            populateCommentMetadata(postOpt, rootComment);
            commentRepository.save(rootComment);
        }
        // save a child level comment here
        else if (parentId != null) {
            Comment comment = new Comment();
            Optional<Comment> parentCommentOpt = commentRepository.findById(parentId);
            if (parentCommentOpt.isPresent())
                comment.setComment(parentCommentOpt.get());
            comment.setText(childCommentText);
            populateCommentMetadata(postOpt, comment);
            commentRepository.save(comment);
        }
    }

    private void populateCommentMetadata(Optional<Post> postOpt, Comment comment) {
        User user = new User(1L, "Shreya");
        if (postOpt.isPresent())
            comment.setPost(postOpt.get());
        comment.setUser(user);
        comment.setCreatedDate(new Date(System.currentTimeMillis()));
    }
}
