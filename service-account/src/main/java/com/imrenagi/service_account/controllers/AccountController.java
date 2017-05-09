package com.imrenagi.service_account.controllers;

import com.imrenagi.service_account.domains.*;
import com.imrenagi.service_account.domains.Number;
import com.imrenagi.service_account.repositories.AccountRepository;
import com.imrenagi.service_account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imrenagi on 5/6/17.
 */
@RestController
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/time", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Time> getTime() {
        return new ResponseEntity<>(new Time(), HttpStatus.OK);
    }

    @RequestMapping(value = "/number", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Number> getNumber() {
        return new ResponseEntity<Number>(new Number(), HttpStatus.OK);
    }

    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<Account>> getAccounts() {
        log.info("API has been called!");
        log.info("Datasource -> " + dataSource);

        List<Account> res = new ArrayList<>();
        Iterable<Account> iterable = accountRepository.findAll();
        iterable.forEach(account -> {
            log.info("Get account -> " + account.getFirstName() + " " + account.getLastName());
            res.add(account);
        });

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(path="/", method = RequestMethod.POST)
    public ResponseEntity<Account> createNewAccount(@Valid @RequestBody User user) {
        log.info("Create new account -> " + user.getUsername() + " - " + user.getPassword());
        return new ResponseEntity<Account>(accountService.create(user), HttpStatus.CREATED);
    }

}
