package com.reddit.RedditClone.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PostVote {

    private Long postId;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String videoUrl;
    private String author;
    private String links;
    private Long subredditId;
    private List<Image> images = new ArrayList<>();
    private Long voteCount = 0L;
    private Long upVoteCount = 0L;
    private Long downVoteCount = 0L;

    private Long voteId;
    private boolean vote;
    private Long userId;
    private boolean contributed;

    public void setPost(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.videoUrl = post.getVideoUrl();
        this.author = post.getAuthor();
        this.links = post.getLinks();
        this.subredditId = post.getSubredditId();
        this.images = post.getImages();
        this.voteCount = post.getVoteCount();
        this.upVoteCount = post.getUpVoteCount();
        this.downVoteCount = post.getDownVoteCount();
    }

    public void setVote(Vote vote){
        this.voteId = vote.getId();
        this.vote = vote.isVote();
        this.userId = vote.getUserId();
        this.contributed = vote.isContributed();
    }


}
