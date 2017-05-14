package com.imrenagi.service_auth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

/**
 * Created by imrenagi on 5/13/17.
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader("Authorization") String auth) {
        if (auth != null) {
            String token = auth.replace("Bearer", "").trim();
            consumerTokenServices.revokeToken(token);
        }
    }

}
