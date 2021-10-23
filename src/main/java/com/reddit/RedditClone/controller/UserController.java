package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.service.PostService;
import com.reddit.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping("/login")
    public String viewLoginPage(Model model){
        System.out.println("loginPage");
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping("/register")
    public String viewRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/saveUser")
    public String registerUser(@ModelAttribute("user") User user, Model model){
        try {
            User newUser = userService.registerUser(user);
            model.addAttribute("user", newUser);
            model.addAttribute("registerMessage", "success");
        }catch (Exception e){
            String errMsg = "Please provide a unique email!!";
            model.addAttribute("errMsg", errMsg);
            return "registration";
        }
        return "login";
    }

    @GetMapping("/access-denied")
    public String viewAccessDeniedPage(){
        System.out.println("Error...");
        return  "error";
    }

    @GetMapping("/user/posts")
    public String getUserPosts(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String errMsg = "Login first to view your profile!!";
            model.addAttribute("errMsg", errMsg);
            return "error";
        }
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        List<Post> posts = postService.findAllNewPostsByUssername(user.getUsername());
        model.addAttribute("posts", posts);

        return "view_profile";
    }
}