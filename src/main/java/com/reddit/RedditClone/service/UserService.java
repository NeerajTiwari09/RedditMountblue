package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User registerUser(User user);
    User findUserByEmail(String email);

}
