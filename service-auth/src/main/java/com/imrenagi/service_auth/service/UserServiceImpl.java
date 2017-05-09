package com.imrenagi.service_auth.service;

import com.imrenagi.service_auth.domain.User;
import com.imrenagi.service_auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by imrenagi on 5/8/17.
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    UserRepository userRepository;

    @Override
    public void create(User user) {

        User existing = userRepository.findOne(user.getUsername());
        Assert.isNull(existing, "user already exist: " + user.getUsername());

        String hash = encoder.encode(user.getPassword());
        user.setPassword(hash);

        userRepository.save(user);

        logger.info("new user has been created {}", user.getUsername());
    }
}
