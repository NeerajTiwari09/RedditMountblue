package com.reddit.RedditClone.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_subreddits")
@Getter @Setter
public class UserSubreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", unique = false)
    private Long userId;
    @Column(name = "subreddit_id", unique = false)
    private Long subredditId;
}
