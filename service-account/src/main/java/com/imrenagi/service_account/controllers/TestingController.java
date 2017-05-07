package com.imrenagi.service_account.controllers;

import com.imrenagi.service_account.domains.Time;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by imrenagi on 5/6/17.
 */
@RestController
public class TestingController {

    @RequestMapping(value = "/time", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Time> getTime() {
        return new ResponseEntity<>(new Time(), HttpStatus.OK);
    }

}
