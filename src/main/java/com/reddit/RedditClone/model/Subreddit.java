package com.reddit.RedditClone.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sub_reddit")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public CommunityType getCommunityType() {
        return communityType;
    }

    public void setCommunityType(CommunityType communityType) {
        this.communityType = communityType;
    }

}