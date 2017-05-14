package com.imrenagi.service_account.controllers;

import com.imrenagi.service_account.controllers.response.AccountResponse;
import com.imrenagi.service_account.domains.Account;
import com.imrenagi.service_account.domains.User;
import com.imrenagi.service_account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by imrenagi on 5/6/17.
 */
@RestController
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<Account> createNewAccount(@Valid @RequestBody User user) {
        return new ResponseEntity<Account>(accountService.create(user), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('READ_BASIC_INFORMATION')")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<AccountResponse> getAllAccounts() {
        AccountResponse response = new AccountResponse();
        response.accounts = accountService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        Account account = accountService.findByUserName(username);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

    @RequestMapping(path = "/current", method = RequestMethod.GET)
    public ResponseEntity<Account> getCurrentAccount(Principal principal) {
        log.info("Get current account with principal {}", principal.getName());
        Account account = accountService.findByUserName(principal.getName());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

}
