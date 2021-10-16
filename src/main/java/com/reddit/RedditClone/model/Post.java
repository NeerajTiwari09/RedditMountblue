package com.reddit.RedditClone.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

//    @LazyCollection(LazyCollectionOption.FALSE)
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinTable(name = "sub_reddit_post", joinColumns = @JoinColumn(name = "id"),
//            inverseJoinColumns = @JoinColumn(name = "sub_reddit_id"))
//    private Subreddit subReddit;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String videoUrl;
    private String author;
    private String links;

    private Long subredditId;

    @Transient
    private MultipartFile image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "post_image_urls", joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images = new ArrayList<>();

    @Column(name = "vote_count", nullable = false)
    private Long voteCount = 0L;

    @Column(name = "up_vote_count", nullable = false)
    private Long upVoteCount = 0L;

    @Column(name = "down_vote_count", nullable = false)
    private Long downVoteCount = 0L;
}
