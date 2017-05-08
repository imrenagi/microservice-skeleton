package com.imrenagi.service_account.repositories;

import com.imrenagi.service_account.domains.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by imrenagi on 5/7/17.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByEmail(String email);
}
