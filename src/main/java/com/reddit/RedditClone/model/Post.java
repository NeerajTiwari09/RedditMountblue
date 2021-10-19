package com.reddit.RedditClone.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "posts")
@Getter @Setter
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String videoUrl;
    private String author;
    private String link;

    private Long subredditId;

    @Transient
    private MultipartFile image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "post_image_urls", joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images = new ArrayList<>();

    @Column(name = "vote_count", columnDefinition = "integer default 0")
    private Long voteCount = 0L;

    @Column(name = "up_vote_count", columnDefinition = "integer default 0")
    private Long upVoteCount = 0L;

    @Column(name = "down_vote_count", columnDefinition = "integer default 0")
    private Long downVoteCount = 0L;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="post")
    @OrderBy("createdDate, id")
    private SortedSet<Comment> comments = new TreeSet<>();
}
