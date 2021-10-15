package com.reddit.RedditClone.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sub_reddit")
@Getter @Setter
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "sub_reddit_type", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private CommunityType communityType;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//        @JoinTable(name = "sub_reddit_post", joinColumns = @JoinColumn(name = "sub_reddit_id"),
//            inverseJoinColumns = @JoinColumn(name = "id"))
    @Transient
    private List<Post> posts;
}