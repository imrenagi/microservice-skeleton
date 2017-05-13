package com.imrenagi.service_auth.service;

import com.imrenagi.service_auth.domain.User;
import com.imrenagi.service_auth.domain.UserRole;
import com.imrenagi.service_auth.repository.UserRepository;
import com.imrenagi.service_auth.repository.UserRoleRepository;
import com.imrenagi.service_auth.utils.UserUtility;
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

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public void create(User user) {

        User existing = userRepository.findByUsername(user.getUsername());
        Assert.isNull(existing, "user already exist: " + user.getUsername());

        String hash = encoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setEnabled(true);

        user = userRepository.save(user);

        UserRole userRole = new UserRole(user.getId(), UserUtility.ROLE_USER);

        userRoleRepository.save(userRole);

        logger.info("new user has been created {}", user.getUsername());

    }
}
