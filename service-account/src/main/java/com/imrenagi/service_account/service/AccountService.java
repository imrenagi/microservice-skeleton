package com.imrenagi.service_account.service;

import com.imrenagi.service_account.domains.Account;
import com.imrenagi.service_account.domains.User;

import java.util.List;

/**
 * Created by imrenagi on 5/8/17.
 */
public interface AccountService {

    Account findByUserName(String username);

    Account findByEmail(String email);

    Account create(User user);

    List<Account> findAll();
}
