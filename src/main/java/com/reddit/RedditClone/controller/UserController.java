package com.reddit.RedditClone.controller;

import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

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
        User newUser = userService.registerUser(user);
        model.addAttribute("user", newUser);
        model.addAttribute("registerMessage", "success");
        return "login";
    }

    @GetMapping("/access-denied")
    public String viewAccessDeniedPage(){
        System.out.println("Error...");
        return  "error";
    }
}