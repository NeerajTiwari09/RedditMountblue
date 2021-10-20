package com.reddit.RedditClone;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.repository.CommentRepository;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    public boolean hasUserId(Authentication authentication, Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        String authorName = userRepository.findByUsername(authentication.getName()).getUsername();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        for(GrantedAuthority auth : authorities){
//            if(auth.getAuthority().equals("ADMIN")){
//                return true;
//            }
//        }
        if(authorName.equals(post.get().getAuthor())){
            System.out.println("Ture");
        }
        else {
            System.out.println("false");
        }
        return post.isPresent() && post.get().getAuthor().equals(authorName);
    }

//    public boolean hasCommentId(Authentication authentication, Integer commentId) {
//        Optional<Comment> comment = commentRepository.findById(commentId);
//        if(hasUserId(authentication, comment.get().getPostId())){
//            return true;
//        }
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        for(GrantedAuthority auth : authorities){
//            if(auth.getAuthority().equals("ADMIN")){
//                return true;
//            }
//        }
//        String email = authentication.getName();
//        return comment.get().getEmail().equals(email);
//    }
}