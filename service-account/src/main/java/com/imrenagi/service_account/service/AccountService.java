package com.imrenagi.service_account.service;

import com.imrenagi.service_account.domains.Account;
import com.imrenagi.service_account.domains.User;

/**
 * Created by imrenagi on 5/8/17.
 */
public interface AccountService {

    Account findByName(String name);

    Account findByEmail(String email);

    Account create(User user);
}
