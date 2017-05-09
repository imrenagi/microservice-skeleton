package com.imrenagi.service_account.service;

import com.imrenagi.service_account.client.AuthServiceClient;
import com.imrenagi.service_account.domains.Account;
import com.imrenagi.service_account.domains.User;
import com.imrenagi.service_account.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by imrenagi on 5/8/17.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AuthServiceClient authClient;

    @Override
    public Account findByName(String name) {
        return null;
    }

    @Override
    public Account findByEmail(String email) {
        Assert.hasLength(email);
        return repository.findByEmail(email);
    }

    @Override
    public Account create(User user) {
        Account existing = repository.findByEmail(user.getUsername());
        Assert.isNull(existing, "account already exist: " + user.getUsername());

        log.info("Trying to create user: " + user.getUsername() + " -> " + user.getPassword());

        authClient.createUser(user);

        Account account = new Account();
        account.setFirstName("first_name");
        account.setLastName("last_name");
        account.setEmail(user.getUsername());

        repository.save(account);

        log.info("New account has been created:" + account.getEmail());

        return account;
    }
}
