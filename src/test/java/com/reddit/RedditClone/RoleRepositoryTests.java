package com.reddit.RedditClone;

import com.reddit.RedditClone.model.Role;
import com.reddit.RedditClone.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void createRoles(){
        Role roleAdmin= new Role("ADMIN");
        Role roleUser = new Role("ROLE_USER");
        Role roleModerator = new Role("MODERATOR");
        roleRepository.saveAll(List.of(roleAdmin, roleUser, roleModerator));
    }

}