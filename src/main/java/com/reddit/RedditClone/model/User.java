package com.reddit.RedditClone.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private Timestamp createdAt;

    @Column(columnDefinition = "integer default 0")
    private Long karma;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
 /*   @JoinTable(name = "user_subreddits",
         joinColumns = {@JoinColumn(name = "user_id")},
         inverseJoinColumns = {@JoinColumn(name = "subreddit_id")})*/
    private Set<Subreddit> subreddits = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  /*  @JoinTable(name = "user_subscription", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "subreddit_id")})*/
    private Set<Subreddit> subredditSubscriptions = new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
    @OrderBy("createdDate, id")
    private SortedSet<Comment> comments = new TreeSet<>();

    public User(Long id, String username){
        this.id = id;
        this.username = username;
    }

    public User(String username){
        this.username = username;
    }
}
