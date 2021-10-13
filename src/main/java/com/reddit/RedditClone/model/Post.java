package com.reddit.RedditClone.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "sub_reddit_post", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "sub_reddit_id"))
    private Set<Subreddit> subRedditId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String videoUrl;
    private String author;
    private String links;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "post_image_urls", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Image> imageUrl;

}
