package com.reddit.RedditClone.repository;


import com.reddit.RedditClone.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role r WHERE r.roleName LIKE %?1%")
    Role findRoleByName(String roleName);
}

