package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Role;
import com.reddit.RedditClone.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(String roleName) {
        return  roleRepository.findRoleByName(roleName);
    }
}