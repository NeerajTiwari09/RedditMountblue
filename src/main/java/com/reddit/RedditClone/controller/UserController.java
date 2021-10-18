package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String viewLoginPage(Model model){

        return "";
    }

    @RequestMapping("/register")
    public String viewRegistrationPage(Model model){

        return "";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user){
        userService.registerUser(user);
        return "";
    }
}