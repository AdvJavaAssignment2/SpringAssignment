package com.service;

import com.models.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User validateUser(String username) {
        return userRepository.findByUserName(username);
    }

    public void addUser(User user) {
        userRepository.addUser(user);
    }
}
