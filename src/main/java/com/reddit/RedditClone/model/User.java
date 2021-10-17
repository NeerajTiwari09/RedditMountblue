package com.reddit.RedditClone.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
//    private Set<Role> authorities = new HashSet<>();
//    @OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY, mappedBy="post")
//    private Set<Post> features = new HashSet<>();

    public User(Long id, String username){
        this.id = id;
        this.username = username;
    }

    public User(String username){
        this.username = username;
    }
}
