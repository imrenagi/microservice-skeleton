package com.imrenagi.service_account.repositories;

import com.imrenagi.service_account.AccountApplication;
import com.imrenagi.service_account.domains.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by imrenagi on 5/14/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountApplication.class)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Test
    public void shouldFindAccountByUsername() {

        Account stub = getStubAccount();
        repository.save(stub);

        Account found = repository.findByUsername(stub.getUsername());
        assertNotNull(stub.getId());
        assertEquals(stub.getFirstName(), found.getFirstName());
        assertEquals(stub.getLastName(), found.getLastName());
        assertEquals(stub.getEmail(), found.getEmail());
        assertEquals(stub.getAddress(), found.getAddress());
        assertEquals(stub.getPhoneNumber(), found.getPhoneNumber());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertNull(found.getDeletedAt());

        repository.delete(found);
    }

    @Test
    public void shouldFindAccountByEmail() {

        Account stub = getStubAccount();
        repository.save(stub);

        Account found = repository.findByEmail(stub.getEmail());
        assertNotNull(stub.getId());
        assertEquals(stub.getFirstName(), found.getFirstName());
        assertEquals(stub.getLastName(), found.getLastName());
        assertEquals(stub.getEmail(), found.getEmail());
        assertEquals(stub.getAddress(), found.getAddress());
        assertEquals(stub.getPhoneNumber(), found.getPhoneNumber());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertNull(found.getDeletedAt());

        repository.delete(found);
    }

    private Account getStubAccount() {

        Account account = new Account();
        account.setUsername("imrenagi");
        account.setFirstName("Imre");
        account.setLastName("Nagi");
        account.setEmail("imrenagi@gmail.com");
        account.setAddress("1600 Villa Street");
        account.setPhoneNumber("+16504354123");

        return account;
    }

}