package com.reddit.RedditClone.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

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

    @Transient
    private List<Post> posts;
}