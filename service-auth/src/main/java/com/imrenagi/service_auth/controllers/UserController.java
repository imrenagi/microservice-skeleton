package com.imrenagi.service_auth.controllers;

import com.imrenagi.service_auth.consumer.GreeterServiceConsumer;
import com.imrenagi.service_auth.domain.User;
import com.imrenagi.service_auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by imrenagi on 5/9/17.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

//    @Autowired
    GreeterServiceConsumer consumer = new GreeterServiceConsumer();

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST)
    public void createUser(@Valid @RequestBody User user) {
        consumer.greet("Imre", "Testing Testing");
        userService.create(user);
    }

}
