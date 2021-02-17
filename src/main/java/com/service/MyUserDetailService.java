package com.service;

import com.models.Authority;
import com.models.Role;
import com.models.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("myUserDetailsService")
public class MyUserDetailService implements UserDetailsService {

    public UserRepository getUserRepository() {
        return userRepository;
    }

    private UserRepository userRepository;

    public User getUser() {
        return user;
    }

    User user;
    @Autowired
    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = userRepository.findByUserName(username);
        Role role = user.getRole();
        String status = user.getEvent();
        Set<Authority> authorities = role.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Authority authority : authorities) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }

        GrantedAuthority roleAuthority = new SimpleGrantedAuthority(role.getName());
        grantedAuthorities.add(roleAuthority);

        if (!status.equals("deleted") || status.equals(null))
            return buildUserForAuthentication(user, grantedAuthorities);
        return buildUserForAuthentication(null,null);
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }
}
